class Day23(input: List<String>) {

    private val board = input
        .flatMapIndexed { y, line -> line.mapIndexed { x, symbol -> Point(x, y) to symbol } }.toMap()

    private val maxX = board.keys.maxOf { point -> point.x }
    private val maxY = board.keys.maxOf { point -> point.y }

    private val start = board.keys.first { point -> point.y == 0 && board[point]!! == '.' }
    private val end = board.keys.first { point -> point.y == maxY && board[point]!! == '.' }

    private val directions = mapOf(
        '>' to listOf(Direction.R),
        '<' to listOf(Direction.L),
        'v' to listOf(Direction.D),
        '^' to listOf(Direction.U),
        '.' to listOf(Direction.L, Direction.R, Direction.U, Direction.D)
    )

    fun part1() {
        solveCrossroads()
        println()
    }

    fun part2() = 2


    private fun solveCrossroads() {
        val slopes = board.keys.filter { point -> board[point] in listOf('<', '>', 'v', '^') } + start + end
        val slopPaths = slopes.associateWith { a -> solveSlope(a, slopes) }
        
        val a = slopPaths.filter { it.key.x == 18 && it.key.y == 19  }
        
        solve(slopPaths)
    }

    private fun solve(slopePaths: Map<Point, List<SlopePath>>) {
        val startPath = Path(start, listOf(start))

        val paths = mutableListOf(startPath)
        val visitedPaths = mutableSetOf(startPath)
        val finishedPaths = mutableListOf<Path>()

        while (paths.isNotEmpty()) {
            val path = paths.removeFirst()
            val possiblePaths = slopePaths[path.position]!!

            possiblePaths.forEach { nextPath ->
                if(!path.points.contains(nextPath.end) && !path.points.contains(nextPath.relevant[0]) ) {
                    val newPath = path.merge(nextPath.end, nextPath.relevant, nextPath.counter)

                    if(newPath.position == end) {
                        finishedPaths += newPath
                    } else {
                        paths += newPath
                        visitedPaths += newPath
                    } 
                }
            }
        }

        val a = finishedPaths.filter { it.position == end }.maxBy { it.counter }
        printPath(a)
        println(a.counter)
        
        println()
    }

    private fun solveSlope(start: Point, other: List<Point>): List<SlopePath> {
        val startPath = Path(start, listOf(start))

        val paths = mutableListOf(startPath)
        val visitedPaths = mutableSetOf(startPath)

        val finishedPaths = mutableListOf<Path>()

        while (paths.isNotEmpty()) {
            val path = paths.removeFirst()
            val tile = board[path.position]!!

            val neighbours = directions[tile]!!
                .map { direction -> path.position + direction }
                .filter { neighbour ->
                    val newTile = board[neighbour]
                    newTile != null && newTile != '#'
                }

            neighbours.forEach { neighbour ->
                if (!path.points.contains(neighbour)) {

                    val newPath = path.go(neighbour)
                    if (!visitedPaths.contains(newPath)) {
                        if (!other.contains(neighbour)) {
                            paths += newPath
                            visitedPaths += newPath
                        } else {
                            finishedPaths += newPath
                        }

                    }
                }
            }
        }

        return finishedPaths.map { path -> SlopePath(start, path.position, path.points.takeLast(2), path.counter) }
    }

    private fun printPath(path: Path) {
        (0..maxY).forEach { y ->
            (0..maxX).forEach { x ->
                val point = Point(x, y)
                val visited = path.points.contains(point)
                if (visited) {
                    print('O')
                } else {
                    print(board[point]!!)
                }
            }
            println()
        }
    }

    data class SlopePath(val start: Point, val end: Point, val relevant: List<Point>, val counter: Int)

    data class Path(val position: Point, val points: List<Point>, val counter: Int = 0) {
        fun go(newPosition: Point) = Path(newPosition, points + newPosition, counter + 1)
        fun merge(newPosition: Point, relevant:List<Point>, newCounter: Int) = 
            Path(newPosition, points + relevant, counter + newCounter)
    }


    data class Point(val x: Int, val y: Int) {
        operator fun plus(other: Direction) = Point(x + other.x, y + other.y)
    }

    enum class Direction(val x: Int, val y: Int) {
        L(-1, 0), R(1, 0), U(0, -1), D(0, 1)
    }
}

fun main() {
//    val input = readText("day23.txt", true)
    val input = readLines("day23.txt")

    val result = Day23(input).part1()
    println(result)
}
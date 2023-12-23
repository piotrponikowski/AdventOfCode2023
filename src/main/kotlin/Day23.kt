import kotlin.math.max

class Day23(input: List<String>) {

    private val board = input
        .flatMapIndexed { y, line -> line.mapIndexed { x, symbol -> Point(x, y) to symbol } }.toMap()

    private val maxX = board.keys.maxOf { point -> point.x }
    private val maxY = board.keys.maxOf { point -> point.y }

    private val start = board.keys.first { point -> point.y == 0 && board[point]!! == '.' }
    private val end = board.keys.first { point -> point.y == maxY && board[point]!! == '.' }

    private val directions = mapOf(
        '>' to listOf(Direction.L, Direction.R, Direction.U, Direction.D),
        '<' to listOf(Direction.L, Direction.R, Direction.U, Direction.D),
        'v' to listOf(Direction.L, Direction.R, Direction.U, Direction.D),
        '^' to listOf(Direction.L, Direction.R, Direction.U, Direction.D),
        '.' to listOf(Direction.L, Direction.R, Direction.U, Direction.D)
    )

    fun part1() {
        solveCrossroads()
        println()
    }

    fun part2() = 2


    private fun solveCrossroads() {
        val slopes = board.keys.filter { point -> board[point] in listOf('<', '>', 'v', '^') } + start + end
        val junctions = board.keys.filter { point ->
            val tile = board[point]!!

            if (tile != '#') {
                val neighbours = directions[tile]!!
                    .map { direction -> point + direction }
                    .filter { neighbour ->
                        val newTile = board[neighbour]
                        newTile != null && newTile != '#'
                    }

                neighbours.size > 2
            } else {
                false
            }
        } + start + end

        val slopPaths = junctions.associateWith { a -> solveSlope(a, junctions) }
//        val a = slopPaths[Point(1, 0)]!![0]!!
        //println(junctions)
        //printPath(listOf(Point(13, 8)))

        solve(slopPaths)
    }

    private fun solve(slopePaths: Map<Point, List<SlopePath>>) {
        val startPath = Path(start, setOf(start))

        val paths = mutableListOf(startPath)
        var maxCounter = 0

        while (paths.isNotEmpty()) {
            val path = paths.removeLast()
            
            val possiblePaths = slopePaths[path.position]!!

            possiblePaths.forEach { nextPath ->
                if (!path.points.contains(nextPath.end)) {
                    val newPath = path.merge(nextPath.end, nextPath.counter)
                    
                    if (newPath.position == end) {
                        if (newPath.counter > maxCounter) {
                            maxCounter = newPath.counter
                            println(maxCounter)
                        }
                    } else {
                        paths += newPath
                    }
                }
            }
        }
    }

    private fun solveSlope(start: Point, other: List<Point>): List<SlopePath> {
        val startPath = Path(start, setOf(start))

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

        return finishedPaths.map { path -> SlopePath(start, path.position, path.counter) }
    }

    data class SlopePath(val start: Point, val end: Point, val counter: Int)

    data class Path(val position: Point, val points: Set<Point>, val counter: Int = 0) {
        fun go(newPosition: Point) = Path(newPosition, points + newPosition, counter + 1)
        fun merge(newPosition: Point, newCounter: Int) =
            Path(newPosition, points + newPosition, counter + newCounter)
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
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
        solve()
        println()
    }

    fun part2() = 2

    private fun solve(): List<Path> {
        val startPath = Path(start, setOf(start))

        var paths = mutableListOf(startPath)
        val visitedPaths = mutableSetOf(startPath)

        while (paths.isNotEmpty()) {
            paths = paths.sortedBy { -it.points.size }.toMutableList()
            
            val path = paths.removeLast()
            val tile = board[path.position]!!

            val neighbours = directions[tile]!!
                .map { direction -> path.position + direction }
                .filter { neighbour ->
                    val newTile = board[neighbour]
                    newTile != null && newTile != '#'
                }

            neighbours.forEach { neighbour ->
                if (!path.points.contains(neighbour)) {

                    val newNeighbours = directions[tile]!!
                        .map { direction -> neighbour + direction }
                        .filter { newNeighbour ->
                            val newTile = board[newNeighbour]
                            newTile != null && newTile != '#'
                        }
                    
                    val checkpoint = newNeighbours.size > 2

                    val newPath = path.go(neighbour, checkpoint)
                    if (!visitedPaths.contains(newPath)) {
                        paths += newPath
                        visitedPaths += newPath
                    }
                    
                    if(neighbour == end) {
                        println(path)
                    }
                }
            }
        }

        val finishedPaths = visitedPaths.filter { it.position == end }.maxBy { it.points.size }
//        printPath(finishedPaths)

        println(finishedPaths.points.size - 1)
        return listOf()
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

    data class Path(val position: Point, val points: Set<Point>, val counter: Int = 0) {
        fun go(newPosition: Point, checkpoint: Boolean): Path {
            return if (checkpoint) {
                Path(newPosition, points + newPosition, counter + 1)
            } else {
                Path(newPosition, points, counter + 1)
            }
        }
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
    val input = readLines("day23.txt", true)

    val result = Day23(input).part1()
    println(result)
}
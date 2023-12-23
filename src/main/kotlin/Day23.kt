class Day23(input: List<String>) {

    private val board =
        input.flatMapIndexed { y, line -> line.mapIndexed { x, symbol -> Point(x, y) to symbol } }.toMap()

    private val forestPoints = board.keys.filter { point -> board[point]!! == '#' }.toSet()
    private val walkablePoints = (board.keys - forestPoints).toSet()
    
    private val maxY = board.keys.maxOf { point -> point.y }
    private val start = board.keys.first { point -> point.y == 0 && board[point]!! == '.' }
    private val end = board.keys.first { point -> point.y == maxY && board[point]!! == '.' }

    private val slopes = mapOf(
        '>' to listOf(Direction.R),
        '<' to listOf(Direction.L),
        'v' to listOf(Direction.D),
        '^' to listOf(Direction.U),
        '.' to listOf(Direction.L, Direction.R, Direction.U, Direction.D)
    )

    fun part1() = solve(false)

    fun part2() = solve(true)

    private fun solve(climbSlopes: Boolean): Int {
        val junctions = findJunctions()
        val routes = junctions.associateWith { junction -> calculateRoutes(junction, junctions, climbSlopes) }
        return calculateMaxPath(routes)
    }

    private fun findJunctions(): List<Point> {
        val junctions = walkablePoints.filter { point ->
            val neighbours = Direction.entries
                .map { direction -> point + direction }
                .map { neighbour -> board[neighbour] }
                .filter { newTile -> newTile != null && newTile != '#' }

            neighbours.size > 2
        }

        return junctions + start + end
    }

    private fun calculateRoutes(start: Point, other: List<Point>, climbSlopes: Boolean): List<Route> {
        val startPath = Path(start, setOf(start))

        val paths = mutableListOf(startPath)
        val validPaths = mutableListOf<Path>()

        while (paths.isNotEmpty()) {
            val path = paths.removeFirst()
            val tile = if(climbSlopes) '.' else board[path.currentPosition]!!

            val neighbours = slopes[tile]!!
                .map { direction -> path.currentPosition + direction }
                .filter { neighbour -> neighbour in walkablePoints }

            neighbours.forEach { neighbour ->
                if (!path.points.contains(neighbour)) {
                    val newPath = Path(neighbour, path.points + neighbour, path.distance + 1)
                    if (other.contains(neighbour)) {
                        validPaths += newPath
                    } else {
                        paths += newPath
                    }
                }
            }
        }

        return validPaths.map { path -> Route(start, path.currentPosition, path.distance) }
    }

    private fun calculateMaxPath(routes: Map<Point, List<Route>>): Int {
        val startPath = Path(start, setOf(start))

        val paths = mutableListOf(startPath)
        var maxCounter = 0

        while (paths.isNotEmpty()) {
            val path = paths.removeLast()
            val possibleRoutes = routes[path.currentPosition]!!

            possibleRoutes.forEach { route ->
                if (!path.points.contains(route.end)) {
                    val newPath = Path(route.end, path.points + route.end, path.distance + route.distance)

                    if (newPath.currentPosition == end) {
                        if (newPath.distance > maxCounter) {
                            maxCounter = newPath.distance
                        }
                    } else {
                        paths += newPath
                    }
                }
            }
        }

        return maxCounter
    }

    data class Route(val start: Point, val end: Point, val distance: Int)

    data class Path(val currentPosition: Point, val points: Set<Point>, val distance: Int = 0)

    data class Point(val x: Int, val y: Int) {
        operator fun plus(other: Direction) = Point(x + other.x, y + other.y)
    }

    enum class Direction(val x: Int, val y: Int) {
        L(-1, 0), R(1, 0), U(0, -1), D(0, 1)
    }
}
import java.math.BigInteger

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
        val routes = calculateRoutes(junctions, climbSlopes)
        return calculateMaxPath(routes)
    }

    private fun findJunctions(): List<Junction> {
        val junctions = walkablePoints.filter { point ->
            val neighbours = Direction.entries
                .map { direction -> point + direction }
                .map { neighbour -> board[neighbour] }
                .filter { newTile -> newTile != null && newTile != '#' }

            neighbours.size > 2
        }

        val allJunctions = junctions + start + end
        return allJunctions.mapIndexed { index, position -> Junction(index, position) }
    }

    private fun calculateRoutes(junctions: List<Junction>, climbSlopes: Boolean): Map<Junction, List<Route>> {
        val positions = junctions.associateBy { junction -> junction.position }
        val routes = mutableMapOf<Junction, List<Route>>()

        positions.keys.forEach { position ->
            val startPath = Path(position, setOf(position))

            val paths = mutableListOf(startPath)
            val validPaths = mutableListOf<Path>()

            while (paths.isNotEmpty()) {
                val path = paths.removeFirst()
                val tile = if (climbSlopes) '.' else board[path.currentPosition]!!

                val neighbours = slopes[tile]!!
                    .map { direction -> path.currentPosition + direction }
                    .filter { neighbour -> neighbour in walkablePoints }

                neighbours.forEach { neighbour ->
                    if (!path.points.contains(neighbour)) {
                        val newPath = Path(neighbour, path.points + neighbour)
                        if (positions.contains(neighbour)) {
                            validPaths += newPath
                        } else {
                            paths += newPath
                        }
                    }
                }
            }

            val start = positions[position]!!
            routes[start] = validPaths.map { path ->
                val end = positions[path.currentPosition]!!
                val distance = path.points.size - 1

                Route(start, end, distance)
            }
        }

        return routes
    }

    private fun calculateMaxPath(routes: Map<Junction, List<Route>>): Int {
        val positions = routes.keys.associateBy { junction -> junction.position }
        val startJunction = positions[start]!!
        val endJunction = positions[end]!!

        val startPath = MaxPath(startJunction, BigInteger.ZERO.setBit(startJunction.id))

        val paths = mutableListOf(startPath)
        var maxCounter = 0

        while (paths.isNotEmpty()) {
            val path = paths.removeLast()
            val possibleRoutes = routes[path.currentJunction]!!

            possibleRoutes.forEach { route ->
                if (!path.visited.testBit(route.end.id)) {
                    val newPath = MaxPath(route.end, path.visited.setBit(route.end.id), path.distance + route.distance)

                    if (newPath.currentJunction == endJunction) {
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

    data class MaxPath(val currentJunction: Junction, val visited: BigInteger, val distance: Int = 0)

    data class Route(val start: Junction, val end: Junction, val distance: Int)

    data class Path(val currentPosition: Point, val points: Set<Point>)

    data class Junction(val id: Int, val position: Point)

    data class Point(val x: Int, val y: Int) {
        operator fun plus(other: Direction) = Point(x + other.x, y + other.y)
    }

    enum class Direction(val x: Int, val y: Int) {
        L(-1, 0), R(1, 0), U(0, -1), D(0, 1)
    }
}
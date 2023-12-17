class Day17(input: List<String>) {

    private val board = input
        .flatMapIndexed { y, line -> line.mapIndexed { x, symbol -> Point(x, y) to symbol.toString().toInt() } }.toMap()

    fun part1() = solve(0, 3)

    fun part2() = solve(4, 10)

    fun solve(minSteps: Int, maxSteps: Int): Int {
        val startingCrucibles = listOf(Crucible(Point(0, 0), Direction.R), Crucible(Point(0, 0), Direction.D))

        val crucibles = startingCrucibles.toMutableList()
        val visited = startingCrucibles.associateWith { 0 }.toMutableMap()

        while (crucibles.isNotEmpty()) {
            val crucible = crucibles.removeFirst()
            val heatLoss = visited[crucible]!!

            crucible.direction.allowedDirections().forEach { direction ->
                val newPosition = crucible.position + direction
                val sameDirection = direction == crucible.direction
                val directionAllowed = if (sameDirection) crucible.steps < maxSteps else crucible.steps >= minSteps

                if (board.containsKey(newPosition) && directionAllowed) {

                    val newCounter = if (direction == crucible.direction) crucible.steps + 1 else 1
                    val newHeatLost = heatLoss + board[newPosition]!!
                    val newCrucible = Crucible(crucible.position + direction, direction, newCounter)

                    val existingHeatLoss = visited[newCrucible] ?: Int.MAX_VALUE
                    if (newHeatLost < existingHeatLoss) {
                        crucibles += newCrucible
                        visited[newCrucible] = newHeatLost
                    }
                }
            }
        }

        val maxX = board.keys.maxOf { point -> point.x }
        val maxY = board.keys.maxOf { point -> point.y }
        val endPosition = Point(maxX, maxY)

        return visited.filter { (crucible) -> crucible.position == endPosition && crucible.steps >= minSteps }.values.min()
    }

    data class Crucible(val position: Point, val direction: Direction, val steps: Int = 0)

    data class Point(val x: Int, val y: Int) {
        operator fun plus(other: Direction) = Point(x + other.x, y + other.y)
    }

    enum class Direction(val x: Int, val y: Int) {
        L(-1, 0),
        R(1, 0),
        U(0, -1),
        D(0, 1);

        fun allowedDirections() = when (this) {
            D -> listOf(D, R, L)
            R -> listOf(R, U, D)
            U -> listOf(U, L, R)
            L -> listOf(L, U, D)
        }
    }
}
class Day17(input: List<String>) {

    private val board = input
        .flatMapIndexed { y, line -> line.mapIndexed { x, symbol -> Point(x, y) to symbol.toString().toInt() } }.toMap()

    fun part1(): Int {
        val maxX = board.keys.maxOf { point -> point.x }
        val maxY = board.keys.maxOf { point -> point.y }

        val startPosition = Point(0, 0)
        val endPosition = Point(maxX, maxY)

        val startingCrucible = Crucible(startPosition, Direction.R, 0)

        val crucibles = mutableListOf(startingCrucible)
        val visited = mutableMapOf(startingCrucible to 0)

        while (crucibles.isNotEmpty()) {
            val crucible = crucibles.removeFirst()
            val heatLost = visited[crucible]!!

            val directions = crucible.direction.allowedDirections()

            directions.forEach { direction ->
                val newPosition = crucible.position + direction
                val newCounter = if (direction == crucible.direction) crucible.steps + 1 else 1
                if (board.containsKey(newPosition) && newCounter <= 3) {

                    val newHeatLost = heatLost + board[newPosition]!!
                    val newCrucible = Crucible(crucible.position + direction, direction, newCounter)
                    val existingHeatLost = visited[newCrucible] ?: Int.MAX_VALUE

                    if (newHeatLost < existingHeatLost) {
                        crucibles += newCrucible
                        visited[newCrucible] = newHeatLost
                    }
                }
            }

        }

        return visited.filter { (crucible) -> crucible.position == endPosition }.values.min()
    }

    fun part2(): Int {
        val startingCrucibles = listOf(Crucible(Point(0, 0), Direction.R), Crucible(Point(0, 0), Direction.D))

        val crucibles = startingCrucibles.toMutableList()
        val visited = startingCrucibles.associateWith { 0 }.toMutableMap()

        while (crucibles.isNotEmpty()) {
            val crucible = crucibles.removeFirst()
            val heatLost = visited[crucible]!!

            val directions = crucible.direction.allowedDirections()

            directions.forEach { direction ->
                val newPosition = crucible.position + direction

                val straightForced = crucible.steps >= 4 || direction == crucible.direction
                val turnForced = crucible.steps < 10 || direction !== crucible.direction

                val newCounter = if (direction == crucible.direction) crucible.steps + 1 else 1

                if (board.containsKey(newPosition) && straightForced && turnForced) {

                    val newHeatLost = heatLost + board[newPosition]!!
                    val newCrucible = Crucible(crucible.position + direction, direction, newCounter)
                    val existingHeatLost = visited[newCrucible] ?: Int.MAX_VALUE

                    if (newHeatLost < existingHeatLost) {
                        crucibles += newCrucible
                        visited[newCrucible] = newHeatLost
                    }
                }
            }

        }

        val maxX = board.keys.maxOf { point -> point.x }
        val maxY = board.keys.maxOf { point -> point.y }
        val endPosition = Point(maxX, maxY)

        return visited.filter { (crucible) -> crucible.position == endPosition && crucible.steps >= 4 }.values.min()
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
class Day17(input: List<String>) {

    private val board = input
        .flatMapIndexed { y, line -> line.mapIndexed { x, symbol -> Point(x, y) to symbol.toString().toInt() } }.toMap()

    fun part1() = solve(0, 3)

    fun part2() = solve(4, 10)

    fun solve(minSteps: Int, maxSteps: Int): Int {
        val startingStates = listOf(State(Point(0, 0), Direction.R), State(Point(0, 0), Direction.D))

        val states = startingStates.toMutableList()
        val visited = startingStates.associateWith { 0 }.toMutableMap()

        while (states.isNotEmpty()) {
            val state = states.removeFirst()
            val heatLoss = visited[state]!!

            state.direction.allowedDirections().forEach { direction ->
                val newPosition = state.position + direction
                val sameDirection = direction == state.direction
                val directionAllowed = if (sameDirection) state.steps < maxSteps else state.steps >= minSteps

                if (board.containsKey(newPosition) && directionAllowed) {

                    val newCounter = if (direction == state.direction) state.steps + 1 else 1
                    val newHeatLost = heatLoss + board[newPosition]!!
                    val newState = State(state.position + direction, direction, newCounter)

                    val existingHeatLoss = visited[newState] ?: Int.MAX_VALUE
                    if (newHeatLost < existingHeatLoss) {
                        states += newState
                        visited[newState] = newHeatLost
                    }
                }
            }
        }

        val maxX = board.keys.maxOf { point -> point.x }
        val maxY = board.keys.maxOf { point -> point.y }
        val endPosition = Point(maxX, maxY)

        return visited.filter { (state) -> state.position == endPosition && state.steps >= minSteps }.values.min()
    }

    data class State(val position: Point, val direction: Direction, val steps: Int = 0)

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
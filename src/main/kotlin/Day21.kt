class Day21(input: List<String>) {

    private val board = input
        .flatMapIndexed { y, line -> line.mapIndexed { x, symbol -> Point(x.toLong(), y.toLong()) to symbol } }.toMap()

    private val startState = board.keys.filter { point -> board[point]!! == 'S' }.toSet()
    private val garden = board.keys.filter { point -> board[point]!! in listOf('S', '.') }.toSet()
    
    private val boardWidth = board.keys.maxOf { point ->  point.x } + 1
    private val boardHeight = board.keys.maxOf { point -> point.y } + 1
    
    private val directions = listOf(Point(-1, 0), Point(1, 0), Point(0, -1), Point(0, 1))
    
    fun part1() = solve(64)

    fun part2(): Long {
        val boardCenter = (boardWidth - 1) / 2

        var step = 0
        var currentState = startState.toSet()
        val counts = mutableListOf<Long>()

        while (counts.size < 3) {
            step++
            currentState = step(currentState)

            if (step % boardWidth == boardCenter) {
                counts += currentState.count().toLong()
            }
        }

        val diffs1 = counts.windowed(2).map { (value1, value2) -> value2 - value1 }
        val diffs2 = diffs1.windowed(2).map { (value1, value2) -> value2 - value1 }

        var result = counts.last()
        var delta1 = diffs1.last()
        val delta2 = diffs2.last()

        val stepsTotal = 26501365
        var stepsCounted = boardCenter + boardWidth + boardWidth

        while (stepsCounted < stepsTotal) {
            delta1 += delta2
            result += delta1
            stepsCounted += boardWidth
        }

        return result
    }

    private fun step(state: Set<Point>): Set<Point> {
        val newState = mutableSetOf<Point>()
        state.map { point ->

            val neighbours = directions
                .map { direction -> point + direction }
                .filter { neighbour ->
                    val x = ((neighbour.x % boardWidth) + boardWidth) % boardWidth
                    val y = ((neighbour.y % boardHeight) + boardHeight) % boardHeight
                    garden.contains(Point(x, y))
                }

            newState += neighbours
        }

        return newState
    }
    
    fun solve(steps: Int): Int {
        var state = startState

        for (i in 1..steps) {
            state = step(state)
        }

        return state.count()
    }

    data class Point(val x: Long, val y: Long) {
        operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    }
}
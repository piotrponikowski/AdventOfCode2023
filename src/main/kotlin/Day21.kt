class Day21(input: List<String>) {

    private val board = input
        .flatMapIndexed { y, line -> line.mapIndexed { x, symbol -> Point(x.toLong(), y.toLong()) to symbol } }.toMap()

    private val startState = board.keys.filter { point -> board[point]!! == 'S' }.toSet()

    private val directions = listOf(Point(-1, 0), Point(1, 0), Point(0, -1),Point(0, 1))
    
    fun part1():Int {
        var state = startState

        for (i in 1..64) {
            state = step2(state)
        }

        return state.count()
    }

    fun part2() :Long {
        val maxX = board.keys.maxOf { point -> point.x }.toInt()
        val boardWidth = maxX + 1
        val boardCenter = (boardWidth - 1) / 2

        var step = 0
        var currentState = startState.toSet()
        val counts = mutableListOf<Long>()
        
        while (counts.size < 3) {
            step++
            currentState = step2(currentState)

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


    private fun step2(state: Set<Point>): Set<Point> {
        val width = board.keys.maxOf { it.x } + 1
        val height = board.keys.maxOf { it.y } + 1

        val newState = mutableSetOf<Point>()
        state.map { point ->

            val neighbours = directions
                .map { direction -> point + direction }
                .filter { neighbour ->
                    val wx = ((neighbour.x % width) + width) % width
                    val wy = ((neighbour.y % height) + height) % height
                    val warpPoint = Point(wx, wy)
                    val warpSymbol = board[warpPoint]!!

                    warpSymbol == '.' || warpSymbol == 'S'
                }

            newState += neighbours
        }

        return newState
    }

    data class Point(val x: Long, val y: Long) {
        operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    }
}
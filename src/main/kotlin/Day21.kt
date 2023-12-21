import kotlin.math.abs
import kotlin.math.max

class Day21(input: List<String>) {

    private val board = input
        .flatMapIndexed { y, line -> line.mapIndexed { x, symbol -> Point(x.toLong(), y.toLong()) to symbol } }.toMap()

    private val steps = board.keys.filter { point -> board[point]!! == 'S' }

    fun part1() {
        var state = board
        (1..10).forEach {
            state = step(state)
            println()
            //printBoard(state)
        }

        val count = state.values.count { it == 'O' }
        println(count)
    }

    fun part2() {
//        var state = steps.toSet()
//        val counts = mutableListOf<Int>()
//        val width = board.keys.maxOf { it.x } + 1
//
//        (0..width * 3 + 65).forEach { step ->
//            state = step2(state)
//            counts += state.count()
//            println("$step -> ${state.count()}")
//        }
//
//        val counts2 = counts.filterIndexed { index, count -> (index+1) % width.toInt() == 65 }
//        val diffs1 = counts2.windowed(2).map { (a, b) -> b - a }
//        val diffs2 = diffs1.windowed(2).map { (a, b) -> b - a }
//
//        println(counts2)
//        println(diffs1)
//        println(diffs2)

        var result = 186885L
        var stepsCounted = 65 + 131+131+131
        val stepsTotal = 26501365
        val loopSize = 131

        var delta1 = 91443L
        var delta2 = 30442L

        //622920842019122
        //622927000436128
        //622933158883576
        //622934175946782
        while (stepsCounted < stepsTotal) {
            delta1 += delta2
            result += delta1
            stepsCounted += loopSize
        }

        println(delta1)
        println(result)
    }


    private fun step2(state: Set<Point>): Set<Point> {
        val width = board.keys.maxOf { it.x } + 1
        val height = board.keys.maxOf { it.y } + 1

        val newState = mutableSetOf<Point>()
        state.map { point ->

            val neighbours = Direction.entries
                .map { direction -> point + direction }
                .filter { neighbour ->
                    val wx = ((neighbour.x % width) + width) % width;
                    val wy = ((neighbour.y % height) + height) % height;
                    val warpPoint = Point(wx, wy)
                    val warpSymbol = board[warpPoint]!!

                    warpSymbol == '.' || warpSymbol == 'S'
                }

            newState += neighbours
        }

        return newState
    }

    private fun step(state: Map<Point, Char>): Map<Point, Char> {
        return state.mapValues { (point, symbol) ->
            val neighbours = Direction.entries
                .map { direction -> point + direction }
                .mapNotNull { neighbour -> state[neighbour] }

            if (symbol == '#') {
                symbol
            } else {
                val mark = neighbours.any { it == 'O' || it == 'S' }
                if (mark) 'O' else '.'
            }

        }
    }

    private fun printBoard2(state: Map<Point, Long>) {
        val maxX = state.keys.maxOf { it.x }
        val maxY = state.keys.maxOf { it.y }


        (0..maxY).forEach { y ->
            (0..maxX).forEach { x ->
                val a = when (state[Point(x, y)]!!) {
                    -1L -> '#'
                    0L -> '.'
                    else -> 'O'
                }
                print(a)
            }
            println()
        }
    }

    private fun printBoard(state: Map<Point, Char>) {
        val maxX = state.keys.maxOf { it.x }
        val maxY = state.keys.maxOf { it.y }


        (0..maxY).forEach { y ->
            (0..maxX).forEach { x ->
                print(state[Point(x, y)]!!)
            }
            println()
        }
    }

    data class Point(val x: Long, val y: Long) {
        operator fun plus(other: Direction) = Point(x + other.x, y + other.y)
    }

    enum class Direction(val x: Long, val y: Long) {
        L(-1, 0), R(1, 0), U(0, -1), D(0, 1)
    }
}

fun main() {
//    val input = readText("day21.txt", true)
    val input = readLines("day21.txt")

    val result = Day21(input).part2()
    println(result)
}
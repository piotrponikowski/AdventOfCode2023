import kotlin.math.max

class Day21(input: List<String>) {

    private val board = input
        .flatMapIndexed { y, line -> line.mapIndexed { x, symbol -> Point(x, y) to symbol } }.toMap()

    private val board2 = board.mapValues { (_, symbol) ->
        when (symbol) {
            '#' -> -1L
            'S' -> 1L
            else -> 0L
        }
    }

    fun part1() {
        var state = board
        (1..6).forEach {
            state = step(state)
            println()
            //printBoard(state)
        }

        val count = state.values.count { it == 'O' }
        println(count)
    }

    fun part2() {
        var state = board2
        (1..10).forEach {
            state = step2(state)
            println()
            printBoard2(state)
        }

        val count = state.values.filter { it > 0L }.sumOf { it }
        println(count)
    }


    private fun step2(state: Map<Point, Long>): Map<Point, Long> {
        val maxX = state.keys.maxOf { it.x }
        val maxY = state.keys.maxOf { it.y }

        return state.mapValues { (point, count) ->
            if (count == -1L) {
                count
            } else {
                val neighbours = Direction.entries
                    .map { direction -> point + direction }

                val baseAny = neighbours
                    .mapNotNull { neighbour -> state[neighbour] }
                    .any { it > 0 }


                val wrappedCount = neighbours.map { neighbour ->
                    when {
                        neighbour.x < 0 -> Point(maxX, neighbour.y)
                        neighbour.x > maxX -> Point(0, neighbour.y)
                        neighbour.y < 0 -> Point(neighbour.x, maxY)
                        neighbour.y > maxY -> Point(neighbour.x, 0)
                        else -> null
                    }
                }.mapNotNull { neighbour -> state[neighbour] }.sumOf { if (it > 0L) it else 0L }


                val baseCount = if (baseAny) 1L else 0L

                baseCount+ wrappedCount
            }
        }
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

    data class Point(val x: Int, val y: Int) {
        operator fun plus(other: Direction) = Point(x + other.x, y + other.y)
    }

    enum class Direction(val x: Int, val y: Int) {
        L(-1, 0), R(1, 0), U(0, -1), D(0, 1)
    }
}

fun main() {
//    val input = readText("day21.txt", true)
    val input = readLines("day21.txt", true)

    val result = Day21(input).part2()
    println(result)
}
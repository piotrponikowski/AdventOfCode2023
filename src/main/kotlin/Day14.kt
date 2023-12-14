import java.util.*

class Day14(input: List<String>) {

    private val board =
        input.flatMapIndexed { y, line -> line.mapIndexed { x, symbol -> Point(x, y) to symbol } }.toMap()

    fun part1(): Int {
        val state = solve(board, Direction.U)
        return scoreState(state)
    }


    data class Cache(val state: Map<Point, Char>, val score: Int)

    fun part2() {
        val scores = mutableListOf<Cache>()
//        scores += Cache(board, 0)

        var current = board
        var found = false;
        var loopStart = -1
        while (!found) {
            current = cycle(current)
            val score = scoreState(current)
            val cacheKey = Cache(current, score)

            found = scores.contains(cacheKey)
            if (!found) {
                scores += cacheKey
            } else {
                loopStart = scores.indexOf(cacheKey)
            }
            //println(scores.indexOf(cacheKey))


//            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
//            val currentDate = sdf.format(Date())
//            println(currentDate)

            println(score)
        }

//        val loopedElement = scores.last()

        val loopEnd = scores.size
        val loopSize = loopEnd - loopStart
        
        val rest = ((1000000000 - loopStart) % loopSize) + loopStart-1

        println()
        println(rest)
        println(loopStart)
        println(scores[rest].score)
    }

    private fun cycle(state: Map<Point, Char>): Map<Point, Char> {
        val state1 = solve(state, Direction.U)
        val state2 = solve(state1, Direction.L)
        val state3 = solve(state2, Direction.D)
        return solve(state3, Direction.R)
    }

    private fun solve(state: Map<Point, Char>, direction: Direction): Map<Point, Char> {
        var current = state
        val maxY = board.keys.maxOf { it.y }

        var noChanges = false
        (0..maxY).forEach { _ ->

            if (!noChanges) {
                noChanges = true
                val copy = current.toMutableMap()

                current.filter { it.value == 'O' }.forEach { (key, symbol) ->
                    val neighbourPoint = key + direction
                    val neighbourEntry = current.entries.find { it.key == neighbourPoint }
                    val neighbourSymbol = neighbourEntry?.value ?: '#'

                    if (symbol == 'O' && neighbourSymbol == '.') {
                        copy[neighbourPoint] = 'O'
                        copy[key] = '.'
                        noChanges = false
                    }
                }
                current = copy
            }
        }


        return current
    }

    private fun scoreState(state: Map<Point, Char>): Int {
        val maxY = board.keys.maxOf { it.y }
        return state.entries.map { (key, symbol) ->
            if (symbol == 'O') {
                (maxY + 1) - key.y
            } else {
                0
            }
        }.sum()
    }

    private fun printState(state: Map<Point, Char>) {
        val maxX = state.keys.maxOf { it.x }
        val maxY = state.keys.maxOf { it.y }

        (0..maxY).forEach { y ->
            (0..maxX).forEach { x ->
                print(state[Point(x, y)])
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
//    val input = readText("day14.txt", true)
    val input = readLines("day14.txt")

    val result = Day14(input).part2()
    println(result)
}
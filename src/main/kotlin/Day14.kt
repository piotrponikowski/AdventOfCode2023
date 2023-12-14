class Day14(input: List<String>) {

    private val board =
        input.flatMapIndexed { y, line -> line.mapIndexed { x, symbol -> Point(x, y) to symbol } }.toMap()

    fun part1() = scoreState(moveBoard(board, Direction.U))

    fun part2(): Int {
        val states = mutableListOf<Map<Point, Char>>()

        var currentState = board
        var loopFound = false

        while (!loopFound) {
            currentState = cycle(currentState)
            loopFound = states.contains(currentState)
            states += currentState
        }

        val loopStart = states.indexOf(states.last())
        val loopEnd = states.size
        val loopSize = loopEnd - loopStart - 1

        val index = ((1000000000 - loopStart) % loopSize) + loopStart - 1
        return scoreState(states[index])
    }

    private fun cycle(state: Map<Point, Char>) =
        listOf(Direction.U, Direction.L, Direction.D, Direction.R).fold(state) { a, b -> moveBoard(a, b) }


    private fun moveBoard(state: Map<Point, Char>, direction: Direction): Map<Point, Char> {
        val newState = state.toMutableMap()
        var changedState: Boolean

        do {
            changedState = false

            newState.forEach { (point, symbol) ->
                if (symbol == 'O') {
                    val neighbourPoint = point + direction
                    val neighbourSymbol = newState[neighbourPoint] ?: '#'

                    if (neighbourSymbol == '.') {
                        newState[neighbourPoint] = 'O'
                        newState[point] = '.'
                        changedState = true
                    }
                }
            }

        } while (changedState)


        return newState
    }

    private fun scoreState(state: Map<Point, Char>): Int {
        val maxY = board.keys.maxOf { point -> point.y }
        return state.map { (point, symbol) -> if (symbol == 'O') (maxY + 1) - point.y else 0 }.sum()
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
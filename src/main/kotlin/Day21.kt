class Day21(input: List<String>) {

    private val board = input
        .flatMapIndexed { y, line -> line.mapIndexed { x, symbol -> Point(x, y) to symbol } }.toMap()

    fun part1() {
        var state = board
        (1..64).forEach { 
            state = step(state)
            println()
            //printBoard(state)
        }
  
        val count = state.values.count { it == 'O' }
        println(count)
    }

    fun part2() = 2


    private fun step(state: Map<Point, Char>): Map<Point, Char> {
       
        return state.mapValues { (point, symbol) ->
            val neighbours = Direction.entries
                .map { direction -> point + direction }
                .mapNotNull { neighbour -> state[neighbour] }

            if(symbol == '#') {
                symbol
            } else {
                val mark = neighbours.any { it == 'O' || it == 'S' }
                if (mark) 'O' else '.'   
            }
      
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
    val input = readLines("day21.txt")

    val result = Day21(input).part1()
    println(result)
}
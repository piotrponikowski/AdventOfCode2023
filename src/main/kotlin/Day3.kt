class Day3(val input: List<String>) {

    private val board = input
        .flatMapIndexed { y, line -> line.mapIndexed { x, symbol -> Point(x, y) to symbol } }
        .filter { (_, symbol) -> symbol != '.' }
        .toMap()

    val directions = (-1..1).flatMap { x -> (-1..1).map { y -> Point(x, y) } }
        .filter { !(it.x == 0 && it.y == 0) }

    fun part1():Int {
        val groups = parse()
        return groups.filter { it.partNumber }.map { it.getNumber(board) }.sum()
    }

    fun part2() :Int {
        val groups = parse()
        groups.forEach { group -> println("${group.getNumber(board)} ${group.stars}") }
        
        val stars = groups.filter { it.stars.isNotEmpty() }
            .flatMap { it.stars }.toSet()

        val values = stars.map { star -> 
            val n = groups.filter { it.stars.contains(star) } 
            if(n.size == 2) {
                n[0].getNumber(board) *n[1].getNumber(board)
            } else {
                0
            }
        }
        
//        val test1 = stars.map { star -> groups.filter { b -> b.symbols.contains(star) } }
        
        return values.sum()
    }

    fun parse(): List<Group> {
        val notGrouped = board.keys.filter { board[it]!!.isDigit() }.toMutableList()
        val groups = mutableListOf<Group>()

        while (notGrouped.isNotEmpty()) {
            val start = notGrouped.removeFirst()
            val group = mutableSetOf(start)
            val stars = mutableSetOf<Point>()
            var partNumber = false

            val nextToCheck = mutableListOf(start)
            while (nextToCheck.isNotEmpty()) {
                val next = nextToCheck.removeFirst()
                group += next

                val neighbours = directions.map { d -> next + d }
                neighbours.forEach { neighbour ->
                    val symbol = board[neighbour]
                    if (symbol != null) {
                        if (symbol.isDigit()) {
                            if (neighbour !in group) {
                                nextToCheck += neighbour
                                notGrouped -= neighbour
                            }
                        } else {
                            partNumber = true
                            if(symbol == '*') {
                                stars += neighbour
                            }
                        }
                    }
                }
            }

            groups += Group(group.toList(), stars, partNumber)
        }

        return groups
    }

    data class Group(val points: List<Point>, val stars:Set<Point>, val partNumber: Boolean) {

        fun getNumber(board: Map<Point, Char>): Int {
            return points.sortedBy { p -> p.x }
                .map { p -> board[p]!! }
                .fold("") { a, b -> (a + b.toString()) }
                .toInt()
        }
    }

    data class Point(val x: Int, val y: Int) {
        operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    }

    enum class Direction(val x: Int, val y: Int) {
        L(-1, 0), R(1, 0), U(0, -1), D(0, 1)
    }
}

fun main() {
//    val input = readText("day3.txt", true)
    val input = readLines("day3.txt" )

    val result = Day3(input).part2()
    println(result)
}
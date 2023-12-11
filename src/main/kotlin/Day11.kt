import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Day11(input: List<String>) {

    private val board = input
        .flatMapIndexed { y, line -> line.mapIndexed { x, symbol -> Point(x, y) to symbol } }
        .filter { (_, symbol) -> symbol != '.' }
        .map { (point, _) -> point }

    private val emptyX = findAxisX()
    private val emptyY = findAxisY()

    fun part1() {
        println("part1")
        val distances = mutableListOf<Distance>()

        board.mapIndexed { index1, point1 ->
            board.mapIndexed { index2, point2 ->
                if(index2 > index1) {
                    distances += Distance(listOf(point1, point2))
                }
            }
        }

        println("part2")
        distances.forEach { distance -> println("$distance ${solve(distance)}") }

        val a = distances.map { solve(it) }.sum()

        println(a)
    }

    fun part2() = 2


    private fun findAxisX(): List<Int> {
        val minX = board.minOf { point -> point.x }
        val maxX = board.maxOf { point -> point.x }
        return (minX..maxX).filter { x -> board.none { point -> point.x == x } }
    }

    private fun findAxisY(): List<Int> {
        val minY = board.minOf { point -> point.y }
        val maxY = board.maxOf { point -> point.y }
        return (minY..maxY).filter { y -> board.none { point -> point.y == y } }
    }

    fun solve(distance: Distance): Long {
        val (a, b) = distance.points

        val minX = min(a.x, b.x)
        val maxX = max(a.x, b.x)
        val minY = min(a.y, b.y)
        val maxY = max(a.y, b.y)

        val diffX = (maxX - minX).toLong()
        val diffY = (maxY - minY).toLong()

        val mul = 1000000-1L
        val addX = emptyX.filter { x -> x in (minX..maxX) }.size.toLong() * mul
        val addY = emptyY.filter { y -> y in (minY..maxY) }.size.toLong() * mul

        return diffX + diffY + addX + addY
    }

    data class Distance(val points: List<Point>)

    data class Point(val x: Int, val y: Int)
}

fun main() {
//    val input = readText("day11.txt", true)
    val input = readLines("day11.txt")

    val result = Day11(input).part1()
    println(result)
}
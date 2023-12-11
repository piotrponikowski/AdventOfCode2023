import kotlin.math.max
import kotlin.math.min

class Day11(input: List<String>) {

    private val board = input
        .flatMapIndexed { y, line -> line.mapIndexed { x, symbol -> Point(x, y) to symbol } }
        .filter { (_, symbol) -> symbol != '.' }
        .map { (point, _) -> point }

    private val galaxyX = emptyLines { point -> point.x }
    private val galaxyY = emptyLines { point -> point.y }

    fun part1() = solve(2L)

    fun part2() = solve(1000_000L)

    private fun emptyLines(selector: (Point) -> Int): List<Int> {
        val min = board.minOf(selector)
        val max = board.maxOf(selector)
        return (min..max).filter { v -> board.none { point -> selector(point) == v } }
    }

    fun solve(expandSize: Long = 1L) = board
        .flatMapIndexed { index1, point1 ->
            board.mapIndexed { index2, point2 -> if (index2 > index1) distance(point1, point2, expandSize) else 0L }
        }.sum()

    private fun distance(from: Point, to: Point, expandSize: Long = 1L): Long {
        val minX = min(from.x, to.x)
        val maxX = max(from.x, to.x)
        val minY = min(from.y, to.y)
        val maxY = max(from.y, to.y)

        val diffX = maxX - minX
        val diffY = maxY - minY

        val galaxyX = galaxyX.filter { x -> x in (minX..maxX) }.size * (expandSize)
        val galaxyY = galaxyY.filter { y -> y in (minY..maxY) }.size * (expandSize)

        return diffX + diffY + galaxyX + galaxyY
    }

    data class Point(val x: Int, val y: Int)
}
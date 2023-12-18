import kotlin.math.abs

class Day18(input: List<String>) {

    private val digPlans = input.map { line -> DigPlan.parse(line) }

    fun part1() = calculateArea(toPoints(digPlans))

    fun part2() = calculateArea(toPoints(digPlans.map { digPlan -> digPlan.decode() }))

    private fun calculateArea(points: List<Point>): Long {
        val pairs = points.reversed().windowed(2)

        val sum1 = pairs.sumOf { (curr, next) -> curr.x * next.y }
        val sum2 = pairs.sumOf { (curr, next) -> curr.y * next.x }
        val area = abs(sum2 - sum1) / 2

        val length = pairs.sumOf { (curr, next) -> abs(next.x - curr.x) + abs(next.y - curr.y) }

        return area + length / 2 + 1
    }

    private fun toPoints(digPlans: List<DigPlan>) = digPlans.fold(listOf(Point(0, 0))) { points, plan ->
        val dx = plan.direction.x * plan.distance
        val dy = plan.direction.y * plan.distance
        val lastPoint = points.last()

        points + Point(lastPoint.x + dx, lastPoint.y + dy)
    }

    data class DigPlan(val direction: Direction, val distance: Int, val color: String) {

        fun decode(): DigPlan {
            val newDistance = color.take(5).toInt(16)
            val newDirection = when (color.last()) {
                '0' -> Direction.R
                '1' -> Direction.D
                '2' -> Direction.L
                '3' -> Direction.U
                else -> throw IllegalStateException()
            }

            return DigPlan(newDirection, newDistance, "")
        }

        companion object {
            fun parse(input: String) = input.split(" ").let { (direction, count, color) ->
                DigPlan(Direction.valueOf(direction), count.toInt(), color.drop(2).dropLast(1))
            }
        }
    }

    data class Point(val x: Long, val y: Long) {
        operator fun plus(other: Direction) = Point(x + other.x, y + other.y)
    }

    enum class Direction(val x: Long, val y: Long) {
        L(-1, 0), R(1, 0), U(0, -1), D(0, 1);
    }
}
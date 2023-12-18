class Day18(input: List<String>) {

    val plans = input.map { line -> Plan.parse(line) }

    fun part1() = solve()


    fun part2() = 2


    fun solve() {
        var currentPosition = Point(0, 0)
        val visited = mutableSetOf(currentPosition)

        plans.forEach { plan ->
            (1..plan.count).forEach { _ ->
                currentPosition += plan.direction
                visited += currentPosition
            }
        }

        val minX = visited.minOf { it.x } - 1
        val maxX = visited.maxOf { it.x } + 1

        val minY = visited.minOf { it.y } - 1
        val maxY = visited.maxOf { it.y } + 1

        print(visited)

        var outside = 0

        val start = Point(minX, minY)
        if (start !in visited) {

            val fill = mutableSetOf<Point>()
            val check = mutableListOf(start)
            while (check.isNotEmpty()) {
                val checkPoint = check.removeFirst()

                Direction.entries.forEach { direction ->
                    val neighbourPoint = checkPoint + direction

                    val inside = neighbourPoint.x in (minX..maxX) && neighbourPoint.y in (minY..maxY)
                    if (inside && neighbourPoint !in visited && neighbourPoint !in fill && neighbourPoint !in check) {
                        check += neighbourPoint
                        fill += neighbourPoint
                        //println("added $neighbourPoint")
                    }
                }
            }

            outside = fill.size
        }


        val dx = maxX - minX+1
        val dy = maxY - minY+1
        val total = dx*dy
        
        println(visited.size)
        println(total - outside)

    }

    fun print(points: Set<Point>) {
        val minX = points.minOf { it.x }
        val maxX = points.maxOf { it.x }

        val minY = points.minOf { it.y }
        val maxY = points.maxOf { it.y }

        (minY..maxY).forEach { y ->
            (minX..maxX).forEach { x ->
                print(if (points.contains(Point(x, y))) "#" else ".")
            }
            println()
        }
    }

    data class Plan(val direction: Direction, val count: Int, val color: String) {

        companion object {
            fun parse(input: String): Plan {
                val (direction, count, color) = input.split(" ")
                return Plan(Direction.valueOf(direction), count.toInt(), color)
            }
        }
    }

    data class Point(val x: Int, val y: Int) {
        operator fun plus(other: Direction) = Point(x + other.x, y + other.y)
    }

    enum class Direction(val x: Int, val y: Int) {
        L(-1, 0), R(1, 0), U(0, -1), D(0, 1);
    }
}

fun main() {
//    val input = readText("day18.txt", true)
    val input = readLines("day18.txt")

    val result = Day18(input).part1()
    println(result)
}
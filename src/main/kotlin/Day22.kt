class Day22(input: List<String>) {

    private val bricks = input.mapIndexed { index, line -> Brick.parse(index, line) }

    fun part1() = solveFallen().count { fallenCount -> fallenCount == 0 }

    fun part2() = solveFallen().sum()

    private fun solveFallen(): List<Int> {
        val mainState = solve(bricks)
        val result = mutableListOf<Int>()

        mainState.forEach { brick ->
            val newState = mainState - brick
            val fallenState = solve(newState)

            val fallen = mainState.subtract(fallenState.toSet()) - brick
            result += fallen.size
        }

        return result
    }

    private fun solve(startingState: List<Brick>): List<Brick> {
        val bricksToFall = startingState.sortedBy { brick -> brick.lowestPosition() }.toMutableList()
        val fallenBricks = mutableListOf<Brick>()

        while (bricksToFall.isNotEmpty()) {
            var brick = bricksToFall.removeFirst()
            var continueFall = true

            while (continueFall) {
                continueFall = false

                if (brick.lowestPosition() > 1) {
                    val noOverlap = fallenBricks.all { other -> brick.noOverlap(other) }
                    if (noOverlap) {
                        brick = brick.fall()
                        continueFall = true
                    }
                }
            }

            fallenBricks += brick
        }

        return fallenBricks
    }

    data class Brick(val id: Int, val points: List<Point>) {

        private val minZ = points.minOf { point -> point.z }
        private val maxZ = points.maxOf { point -> point.z }

        fun fall() = Brick(id, points.map { point -> Point(point.x, point.y, point.z - 1) })

        fun lowestPosition() = minZ

        fun noOverlap(other: Brick) = (minZ - 1 > other.maxZ || maxZ < other.minZ || points
            .none { point -> other.points.contains(Point(point.x, point.y, point.z - 1)) })

        companion object {
            fun parse(index: Int, input: String): Brick {
                val (start, end) = input.split("~")
                    .map { pointsInput -> pointsInput.split(",").map { it.toLong() } }
                    .map { (x, y, z) -> Point(x, y, z) }

                val rangeX = if (end.x < start.x) end.x..start.x else start.x..end.x
                val rangeY = if (end.y < start.y) end.y..start.y else start.y..end.y
                val rangeZ = if (end.z < start.z) end.z..start.z else start.z..end.z

                val points = rangeX.flatMap { x ->
                    rangeY.flatMap { y ->
                        rangeZ.map { z -> Point(x, y, z) }
                    }
                }

                return Brick(index + 1, points)
            }
        }
    }

    data class Point(val x: Long, val y: Long, val z: Long) {
        operator fun plus(other: Point) = Point(x + other.x, y + other.y, z + other.z)
    }
}
class Day22(input: List<String>) {

    private val bricks = input
        .mapIndexed { index, line -> Brick.parse(index, line) }
        .sortedBy { brick -> brick.minZ }

    fun part1() = solveFallen().count { it == 0 }
    
    fun part2() = solveFallen().sum()

    private fun solveFallen(): List<Int> {
        val mainState = solve(bricks)
        val result = mutableListOf<Int>()

        mainState.forEach { brick ->
            val newState = mainState - brick
            val fallenState = solve(newState)

            val fallen = mainState.subtract(fallenState.toSet()) - brick
            println(fallen.size)
            result += fallen.size
        }

        return result
    }

    private fun solve(startingState: List<Brick>): List<Brick> {
        val state = startingState.associateBy { brick -> brick.id }.toMutableMap()

        var changed = true
        while (changed) {
            changed = false

            state.keys.forEach { brickId ->
                var brick = state[brickId]!!
                var reCheck = true

                while (reCheck) {
                    reCheck = false

                    if (brick.minZ > 1) {
                        val fallenBrick = brick.fall()
                        val noOverlap = state.values.none { other -> fallenBrick.overlap(other) }

                        if (noOverlap) {
                            state[brickId] = fallenBrick
                            brick = fallenBrick
                            changed = true
                            reCheck = true
                        }
                    }
                }
            }
        }

        return state.values.toList()
    }

    data class Brick(val id: Int, val points: List<Point>) {
        fun fall() = Brick(id, points.map { point -> Point(point.x, point.y, point.z - 1) })
        fun overlap(other: Brick) = id != other.id && bottomPoints.any { point -> other.topPoints.contains(point) }

        val minZ = points.minOf { point -> point.z }

        private val maxZ = points.maxOf { point -> point.z }

        private val bottomPoints = points.filter { point -> point.z == minZ }

        private val topPoints = points.filter { point -> point.z == maxZ }

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
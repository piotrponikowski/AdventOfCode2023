class Day22(input: List<String>) {

    private val bricks = input.mapIndexed { index, line -> Brick.parse(index, line) }

    fun part1() = disintegrate().count { fallenCount -> fallenCount == 0 }

    fun part2() = disintegrate().sum()

    private fun disintegrate(): List<Int> {
        val mainState = processFall(bricks)
        val fallenCounts = mutableListOf<Int>()

        mainState.forEach { brickToDisintegrate ->
            val stateAfterFall = processFall(mainState, brickToDisintegrate)
            val fallenBricks = mainState - stateAfterFall.toSet() - brickToDisintegrate
            fallenCounts += fallenBricks.size
        }

        return fallenCounts
    }

    private fun processFall(startingState: List<Brick>, brickToDisintegrate: Brick? = null): List<Brick> {
        val toFallIndex = if (brickToDisintegrate != null) startingState.indexOf(brickToDisintegrate) + 1 else 0
        val fallenIndex = if (brickToDisintegrate != null) startingState.indexOf(brickToDisintegrate) else 0

        val bricksToFall = startingState.drop(toFallIndex).sortedBy { brick -> brick.lowestPoint() }.toMutableList()
        val fallenBricks = startingState.take(fallenIndex).toMutableList()

        while (bricksToFall.isNotEmpty()) {
            var brick = bricksToFall.removeFirst()
            var continueFall = true

            while (continueFall) {
                continueFall = false

                if (brick.lowestPoint() > 1) {
                    val fallenBrick = brick.fall()
                    val overlaps = fallenBricks.any { other -> fallenBrick.overlaps(other) }

                    if (!overlaps) {
                        brick = fallenBrick
                        continueFall = true
                    }
                }
            }

            fallenBricks += brick
        }

        return fallenBricks
    }

    data class Brick(val id: Int, val x: IntRange, val y: IntRange, val z: IntRange) {

        fun fall() = Brick(id, x, y, z.first - 1..<z.last)

        fun lowestPoint() = z.first

        fun overlaps(other: Brick) = rangeOverlaps(z, other.z) && rangeOverlaps(y, other.y) && rangeOverlaps(x, other.x)

        private fun rangeOverlaps(r1: IntRange, r2: IntRange) = r1.first <= r2.last && r1.last >= r2.first

        companion object {
            fun parse(index: Int, input: String): Brick {
                val (start, end) = input.split("~")
                    .map { points -> points.split(",").map { point -> point.toInt() } }

                val (x1, y1, z1) = start
                val (x2, y2, z2) = end

                val x = if (x1 < x2) x1..x2 else x2..x1
                val y = if (y1 < y2) y1..y2 else y2..y1
                val z = if (z1 < z2) z1..z2 else z2..z1

                return Brick(index + 1, x, y, z)
            }
        }
    }
}
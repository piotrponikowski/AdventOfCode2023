class Day24(input: List<String>) {

    private val hailstones = input.map { line -> Hailstone.parse(line) }
    private val maxX = hailstones.maxOf { hailstone -> hailstone.position.x }
    
    private val collisionRange = if(maxX < 100) 7L..27L else 200000000000000L..400000000000000L

    fun part1(): Int {
        var counter = 0
      
        hailstones.forEachIndexed { index1, hailstone1 ->
            hailstones.forEachIndexed { index2, hailstone2 ->
                if (index2 > index1) {
                    if (checkCollision(hailstone1, hailstone2, collisionRange)) {
                        counter += 1
                    }
                }
            }
        }
        
        return counter
    }

    private fun checkCollision(h1: Hailstone, h2: Hailstone, collisionRange: LongRange): Boolean {
        val p1 = h1.position
        val p2 = h2.position

        val v1 = h1.velocity
        val v2 = h2.velocity

        val denominator = (v1.y * v2.x) - (v1.x * v2.y)
        if (denominator == 0L) {
            return false
        }

        val tNumerator = v2.y * (p1.x - p2.x) - v2.x * (p1.y - p2.y)
        val sNumerator = v1.y * (p1.x - p2.x) - v1.x * (p1.y - p2.y)

        val t = tNumerator.toDouble() / denominator.toDouble()
        val s = sNumerator.toDouble() / denominator.toDouble()

        if (t < 0 || s < 0) {
            return false
        }

        val collisionX = p1.x + v1.x * t
        val collisionY = p1.y + v1.y * t

        val validX = collisionX >= collisionRange.first && collisionX <= collisionRange.last
        val validY = collisionY >= collisionRange.first && collisionY <= collisionRange.last

        return validX && validY
    }

    fun part2() = 2

    data class Hailstone(val position: Point, val velocity: Point) {

        companion object {
            private val PATTERN = Regex("""(-?\d+)""")
            fun parse(input: String): Hailstone {
                val numbers = PATTERN.findAll(input)
                    .map { group -> group.value }.toList()
                    .map { number -> number.toLong() }

                val (x, y, z) = numbers.take(3)
                val (vx, vy, vz) = numbers.takeLast(3)

                return Hailstone(Point(x, y, z), Point(vx, vy, vz))
            }
        }
    }

    data class Point(val x: Long, val y: Long, val z: Long) {
        operator fun plus(other: Point) = Point(x + other.x, y + other.y, z + other.z)
    }
}
import kotlin.math.abs

class Day24(input: List<String>) {

    private val hailstones = input.map { line -> Hailstone.parse(line) }
    private val maxX = hailstones.maxOf { hailstone -> hailstone.position.x }

    private val collisionRange = if (maxX < 100) 7L..27L else 200000000000000L..400000000000000L

    fun part1(): Int {
        var counter = 0

        hailstones.forEachIndexed { index1, hailstone1 ->
            hailstones.forEachIndexed { index2, hailstone2 ->
                if (index2 > index1) {
                    val collision = findCollision(hailstone1, hailstone2)
                    if (collision != null && inCollisionRange(collision)) {
                        counter += 1
                    }
                }
            }
        }

        return counter
    }

    fun part2() {
        val scanRange = (-500L..500L)

        scanRange.forEach { x ->
            scanRange.forEach { y ->
                val velocity = Point(x, y, 0)
                val result = validateVelocity(velocity)
                if(result) {
                    println("$x, $y")
                }
            }
        }
    }

    private fun validateVelocity(velocity: Point): Boolean {
        val (h1, h2, h3) = hailstones

        val hd1 = Hailstone(h1.position, h1.velocity - velocity)
        val hd2 = Hailstone(h2.position, h2.velocity - velocity)
        val hd3 = Hailstone(h3.position, h3.velocity - velocity)

        val c1 = findCollision(hd1, hd2)
        val c2 = findCollision(hd1, hd3)
        val c3 = findCollision(hd2, hd3)

        if (c1 != null && c2 != null && c3 != null) {
            return c1.matches(c2) && c1.matches(c3) && c2.matches(c3)
        } else {
            return false
        }
    }

    private fun findCollision(h1: Hailstone, h2: Hailstone): Collision? {
        val p1 = h1.position
        val p2 = h2.position

        val v1 = h1.velocity
        val v2 = h2.velocity

        val denominator = (v1.y * v2.x) - (v1.x * v2.y)

        if (denominator == 0L) {
            return null
        }

        val tNumerator = v2.y * (p1.x - p2.x) - v2.x * (p1.y - p2.y)
        val sNumerator = v1.y * (p1.x - p2.x) - v1.x * (p1.y - p2.y)

        val t = tNumerator.toDouble() / denominator.toDouble()
        val s = sNumerator.toDouble() / denominator.toDouble()

        if (t < 0 || s < 0) {
            return null
        }

        val collisionX = p1.x + v1.x * t
        val collisionY = p1.y + v1.y * t

        return Collision(collisionX, collisionY)
    }

    private fun inCollisionRange(collision: Collision): Boolean {
        val validX = collision.x >= collisionRange.first && collision.x <= collisionRange.last
        val validY = collision.y >= collisionRange.first && collision.y <= collisionRange.last
        return validX && validY
    }

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
        operator fun minus(other: Point) = Point(x - other.x, y - other.y, z - other.z)
    }

    data class Collision(val x: Double, val y: Double) {
        fun matches(other: Collision): Boolean {
            return abs(x - other.x) < EPSILON && abs(y - other.y) < EPSILON
        }

        companion object {
            const val EPSILON = 0.01
        }
    }
}
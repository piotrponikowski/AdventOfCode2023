import java.math.BigDecimal

class Day24(input: List<String>) {

    private val hailstones = input.map { line -> Hailstone3d.parse(line) }
    private val maxX = hailstones.maxOf { hailstone -> hailstone.position.x }

    private val collisionRange = if (maxX < BigDecimal.valueOf(100)) 7L..27L else 200000000000000L..400000000000000L

    fun part1(): Int {
        var counter = 0
        val testHailstones = hailstones.map { hailstone -> hailstone.toHailstoneXY() }

        testHailstones.forEachIndexed { index1, hailstone1 ->
            testHailstones.forEachIndexed { index2, hailstone2 ->
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

    fun part2(): Long {
        val rockVelocity = findRockVelocity()
        val rockPosition = findRockPosition(rockVelocity)
        return (rockPosition.x + rockPosition.y + rockPosition.z).toLong()
    }

    private fun findRockVelocity(): Point3d {
        val velocityScanRange = (-500L..500L).map { velocity -> velocity.toBigDecimal() }

        val testHailstones = hailstones.take(3)
        val hailstonesXY = testHailstones.map { hailstone -> hailstone.toHailstoneXY() }
        val hailstonesXZ = testHailstones.map { hailstone -> hailstone.toHailstoneXZ() }
        val hailstonesYZ = testHailstones.map { hailstone -> hailstone.toHailstoneYZ() }

        velocityScanRange.forEach { x ->
            velocityScanRange.forEach { y ->
                val validXY = validateRockVelocity(hailstonesXY, Point2d(x, y))

                if (validXY) {
                    velocityScanRange.forEach { z ->
                        val validXZ = validateRockVelocity(hailstonesXZ, Point2d(x, z))
                        val validYZ = validateRockVelocity(hailstonesYZ, Point2d(y, z))

                        if (validYZ && validXZ) {
                            return Point3d(x, y, z)
                        }
                    }
                }
            }
        }

        throw IllegalStateException()
    }

    private fun findRockPosition(velocity: Point3d): Point3d {
        val (h1, h2) = hailstones.take(2)

        val (x, y) = findPartialRockPosition(h1.toHailstoneXY(), h2.toHailstoneXY(), Point2d(velocity.x, velocity.y))!!
        val (_, z) = findPartialRockPosition(h1.toHailstoneYZ(), h2.toHailstoneYZ(), Point2d(velocity.y, velocity.z))!!

        return Point3d(x, y, z)
    }

    private fun findPartialRockPosition(h1: Hailstone2d, h2: Hailstone2d, velocity: Point2d): Collision? {
        val vxy = Point2d(velocity.x, velocity.y)

        val hd1 = Hailstone2d(h1.position, h1.velocity - vxy)
        val hd2 = Hailstone2d(h2.position, h2.velocity - vxy)

        return findCollision(hd1, hd2)
    }

    private fun validateRockVelocity(hailstones: List<Hailstone2d>, velocity: Point2d): Boolean {
        val (h1, h2, h3) = hailstones

        val hd1 = Hailstone2d(h1.position, h1.velocity - velocity)
        val hd2 = Hailstone2d(h2.position, h2.velocity - velocity)
        val hd3 = Hailstone2d(h3.position, h3.velocity - velocity)

        val c1 = findCollision(hd1, hd2)
        val c2 = findCollision(hd1, hd3)
        val c3 = findCollision(hd2, hd3)

        return if (c1 != null && c2 != null && c3 != null) {
            c1.matches(c2) && c1.matches(c3) && c2.matches(c3)

        } else if (c1 != null && c2 != null) {
            c1.matches(c2)

        } else if (c1 != null && c3 != null) {
            c1.matches(c3)

        } else if (c2 != null && c3 != null) {
            c2.matches(c3)

        } else {
            false
        }
    }

    private fun findCollision(h1: Hailstone2d, h2: Hailstone2d): Collision? {
        val p1 = h1.position
        val p2 = h2.position

        val v1 = h1.velocity
        val v2 = h2.velocity

        val denominator = (v1.y * v2.x) - (v1.x * v2.y)

        if (denominator == BigDecimal.ZERO) {
            return null
        }

        val tNumerator = v2.y * (p1.x - p2.x) - v2.x * (p1.y - p2.y)
        val sNumerator = v1.y * (p1.x - p2.x) - v1.x * (p1.y - p2.y)

        val t = tNumerator / denominator
        val s = sNumerator / denominator

        if (t < BigDecimal.ZERO || s < BigDecimal.ZERO) {
            return null
        }

        val collisionX = p1.x + v1.x * t
        val collisionY = p1.y + v1.y * t

        return Collision(collisionX, collisionY)
    }

    private fun inCollisionRange(collision: Collision): Boolean {
        val validX = collision.x.toLong() >= collisionRange.first && collision.x.toLong() <= collisionRange.last
        val validY = collision.y.toLong() >= collisionRange.first && collision.y.toLong() <= collisionRange.last
        return validX && validY
    }

    data class Hailstone2d(val position: Point2d, val velocity: Point2d)

    data class Hailstone3d(val position: Point3d, val velocity: Point3d) {

        fun toHailstoneXY() = Hailstone2d(Point2d(position.x, position.y), Point2d(velocity.x, velocity.y))

        fun toHailstoneXZ() = Hailstone2d(Point2d(position.x, position.z), Point2d(velocity.x, velocity.z))

        fun toHailstoneYZ() = Hailstone2d(Point2d(position.y, position.z), Point2d(velocity.y, velocity.z))

        companion object {
            private val PATTERN = Regex("""(-?\d+)""")
            fun parse(input: String): Hailstone3d {
                val numbers = PATTERN.findAll(input)
                    .map { group -> group.value }.toList()
                    .map { number -> number.toBigDecimal() }

                val (x, y, z) = numbers.take(3)
                val (vx, vy, vz) = numbers.takeLast(3)

                return Hailstone3d(Point3d(x, y, z), Point3d(vx, vy, vz))
            }
        }
    }

    data class Point3d(val x: BigDecimal, val y: BigDecimal, val z: BigDecimal) {
        operator fun minus(other: Point3d) = Point3d(x - other.x, y - other.y, z - other.z)
    }

    data class Point2d(val x: BigDecimal, val y: BigDecimal) {
        operator fun minus(other: Point2d) = Point2d(x - other.x, y - other.y)
    }

    data class Collision(val x: BigDecimal, val y: BigDecimal) {
        fun matches(other: Collision): Boolean {
            return (x - other.x).abs() < EPSILON && (y - other.y).abs() < EPSILON
        }

        companion object {
            val EPSILON: BigDecimal = BigDecimal.valueOf(0.01)
        }
    }
}
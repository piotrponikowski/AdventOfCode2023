class Day16(input: List<String>) {

    private val board = input
        .flatMapIndexed { y, line -> line.mapIndexed { x, symbol -> Point(x, y) to symbol } }.toMap()

    fun part1() = solve(Beam(Point(0, 0), Direction.R))

    fun part2() = staringPositions().maxOfOrNull { beam -> solve(beam) }!!

    private fun staringPositions(): List<Beam> {
        val beams = mutableListOf<Beam>()

        val maxX = board.keys.maxOf { point -> point.x }
        val maxY = board.keys.maxOf { point -> point.y }

        (0..maxX).forEach { x ->
            beams += Beam(Point(x, 0), Direction.D)
            beams += Beam(Point(x, maxY), Direction.U)
        }

        (0..maxY).forEach { y ->
            beams += Beam(Point(0, y), Direction.R)
            beams += Beam(Point(maxX, y), Direction.L)
        }

        return beams
    }

    private fun solve(start: Beam): Int {
        val visited = mutableSetOf<Beam>()
        val beams = mutableListOf(start)

        while (beams.isNotEmpty()) {
            val beam = beams.removeFirst()

            if (!visited.contains(beam) && board.containsKey(beam.position)) {
                visited += beam

                val tile = board[beam.position]!!

                if (tile == '.') {
                    beams += beam.goStraight()
                } else if (tile == '/') {
                    beams += beam.golLeft()
                } else if (tile == '\\') {
                    beams += beam.goRight()
                } else if (tile == '-' && beam.direction.horizontal) {
                    beams += beam.goStraight()
                } else if (tile == '|' && !beam.direction.horizontal) {
                    beams += beam.goStraight()
                } else {
                    beams += beam.golLeft()
                    beams += beam.goRight()
                }
            }
        }

        return visited.map { beam -> beam.position }.toSet().size
    }

    data class Beam(val position: Point, val direction: Direction) {
        fun goStraight() = Beam(position + direction, direction)

        fun golLeft() = Beam(position + direction.turnLeft(), direction.turnLeft())

        fun goRight() = Beam(position + direction.turnRight(), direction.turnRight())
    }

    data class Point(val x: Int, val y: Int) {
        operator fun plus(other: Direction) = Point(x + other.x, y + other.y)
    }

    enum class Direction(val x: Int, val y: Int, val horizontal: Boolean) {
        L(-1, 0, true),
        R(1, 0, true),
        U(0, -1, false),
        D(0, 1, false);

        fun turnLeft() = when (this) {
            D -> L
            R -> U
            U -> R
            L -> D
        }

        fun turnRight() = when (this) {
            D -> R
            R -> D
            U -> L
            L -> U
        }
    }
}
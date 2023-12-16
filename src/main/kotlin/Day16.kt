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
                    beams += beam.straight()
                } else if (tile == '/') {
                    beams += beam.left()
                } else if (tile == '\\') {
                    beams += beam.right()
                } else if (tile == '-' && beam.isHorizontal()) {
                    beams += beam.straight()
                } else if (tile == '|' && beam.isVertical()) {
                    beams += beam.straight()
                } else {
                    beams += beam.left()
                    beams += beam.right()
                }
            }
        }

        return visited.map { it.position }.toSet().size
    }

    data class Beam(val position: Point, val direction: Direction) {
        fun straight() = Beam(position + direction, direction)

        fun left() = Beam(position + direction.left(), direction.left())

        fun right() = Beam(position + direction.right(), direction.right())

        fun isHorizontal() = direction == Direction.L || direction == Direction.R

        fun isVertical() = direction == Direction.U || direction == Direction.D
    }

    data class Point(val x: Int, val y: Int) {
        operator fun plus(other: Direction) = Point(x + other.x, y + other.y)
    }

    enum class Direction(val x: Int, val y: Int) {
        L(-1, 0), R(1, 0), U(0, -1), D(0, 1);

        fun left() = when (this) {
            D -> L
            R -> U
            U -> R
            L -> D
        }

        fun right() = when (this) {
            D -> R
            R -> D
            U -> L
            L -> U
        }
    }
}
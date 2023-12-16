import java.lang.IllegalStateException

class Day16(input: List<String>) {

    private val board = input
        .flatMapIndexed { y, line -> line.mapIndexed { x, symbol -> Point(x, y) to symbol } }.toMap()

    fun part1() = solve(Beam(Point(0, 0), Direction.R))

    fun part2() = staringPositions().map { solve(it) }.max()

    fun staringPositions():List<Beam> {
        val beams = mutableListOf<Beam>()

        val maxX = board.keys.maxOf { it.x }
        val maxY = board.keys.maxOf { it.y }

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

    fun solve(start: Beam): Int {
        val visited = mutableSetOf<Beam>()
        val beams = mutableListOf(start)

        while (beams.isNotEmpty()) {
            val beam = beams.removeFirst()

            if (beam !in visited && board.containsKey(beam.position)) {
                visited += beam

                val tile = board[beam.position]!!

                if (tile == '.') {
                    beams += beam.straight()
                } else if (tile == '/') {
                    beams += beam.left()
                } else if (tile == '\\') {
                    beams += beam.right()
                } else if (tile == '-' && (beam.direction == Direction.L || beam.direction == Direction.R)) {
                    beams += beam.straight()
                } else if (tile == '|' && (beam.direction == Direction.U || beam.direction == Direction.D)) {
                    beams += beam.straight()
                } else if (tile == '-' || tile == '|') {
                    beams += beam.left()
                    beams += beam.right()
                } else {
                    throw IllegalStateException()
                }

            }
        }

        return visited.map { it.position }.toSet().size
    }

    data class Beam(val position: Point, val direction: Direction) {
        fun straight() = Beam(position + direction, direction)

        fun left() = Beam(position + direction.left(), direction.left())

        fun right() = Beam(position + direction.right(), direction.right())

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

fun main() {
//    val input = readText("day16.txt", true)
    val input = readLines("day16.txt")

    val result = Day16(input).part2()
    println(result)
}
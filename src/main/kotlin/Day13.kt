import kotlin.math.min

class Day13(input: String) {

    private val boards = groupLines(input).map { lines -> parseBoard(lines) }

    fun part1() = scoreMirrors(0)

    fun part2() = scoreMirrors(1)

    private fun scoreMirrors(allowedErrors: Int) = boards
        .sumOf { mirror -> findMirror(mirror.transpose(), allowedErrors) * 100 + findMirror(mirror, allowedErrors) }

    private fun findMirror(board: Board, allowedErrors: Int): Int {
        val maxX = board.rocks.keys.maxOf { point -> point.x }
        val maxY = board.rocks.keys.maxOf { point -> point.y }

        val mirror = (0..<maxX).find { mirrorX ->
            val commonWidth = min(mirrorX, maxX - mirrorX - 1)

            val incorrectMatches = (0..maxY).sumOf { y ->
                (0..commonWidth).count { distanceFromMirror ->
                    
                    val leftPoint = Point(mirrorX - distanceFromMirror, y)
                    val rightPoint = Point(mirrorX + distanceFromMirror + 1, y)

                    val left = board.rocks[leftPoint]!!
                    val right = board.rocks[rightPoint]!!

                    left != right
                }
            }

            incorrectMatches == allowedErrors
        }

        return if (mirror != null) mirror + 1 else 0
    }

    private fun parseBoard(input: List<String>) = input
        .flatMapIndexed { y, line -> line.mapIndexed { x, symbol -> Point(x, y) to symbol } }.toMap()
        .mapValues { (_, symbol) -> symbol == '#' }
        .let { points -> Board(points) }

    data class Board(val rocks: Map<Point, Boolean>) {

        fun transpose() = Board(rocks.mapKeys { (point) -> Point(point.y, point.x) })
    }

    data class Point(val x: Int, val y: Int)
}
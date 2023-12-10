class Day10(input: List<String>) {

    private val board = input
        .flatMapIndexed { y, line -> line.mapIndexed { x, symbol -> Point(x, y) to symbol } }.toMap()

    fun part1() = parsePipes().let { (pipes, startingPipe) -> solveLoop(pipes, startingPipe).size / 2 }

    fun part2(): Int {
        val (pipes, startingPipe) = parsePipes()
        val loop = solveLoop(pipes, startingPipe)

        val typesFacingNorth = listOf('|', 'J', 'L')
        val positionsToScan = board.keys - loop.toSet()

        val crossings =
            loop.filter { position -> pipes.first { pipe -> pipe.position == position }.symbol in typesFacingNorth }

        return positionsToScan
            .map { position -> crossings.count { pipe -> pipe.y == position.y && pipe.x < position.x } }
            .count { crossingsCount -> crossingsCount % 2 == 1 }
    }


    private val pipeTypes = mapOf(
        '|' to listOf(Direction.U, Direction.D),
        '-' to listOf(Direction.L, Direction.R),
        'L' to listOf(Direction.U, Direction.R),
        'J' to listOf(Direction.U, Direction.L),
        '7' to listOf(Direction.D, Direction.L),
        'F' to listOf(Direction.D, Direction.R)
    )

    private fun solveLoop(pipes: List<Pipe>, startingPipe: Pipe): List<Point> {
        val visitedPositions = mutableListOf<Point>()
        var currentPipe = startingPipe

        do {
            visitedPositions += currentPipe.position

            val nextPositions = currentPipe.connections.filter { connection -> connection !in visitedPositions }
            val loopEnd = nextPositions.isEmpty() && currentPipe.connections.contains(startingPipe.position)

            currentPipe = if (loopEnd) startingPipe else pipes.first { pipe -> pipe.position == nextPositions.first() }

        } while (currentPipe != startingPipe)

        return visitedPositions
    }

    private fun parsePipes(): Pair<List<Pipe>, Pipe> {
        val pipes = board.filter { (_, symbol) -> symbol in pipeTypes.keys }
            .map { (position, symbol) -> parsePipe(position, symbol) }

        val startingPosition = board.entries.first { (_, symbol) -> symbol == 'S' }.key

        val startingConnections = pipes
            .filter { pipe -> startingPosition in pipe.connections }.map { pipe -> pipe.position }

        val startingSymbol = pipeTypes
            .mapValues { (_, directions) -> directions.map { direction -> startingPosition + direction } }
            .filter { (_, connections) -> startingConnections.containsAll(connections) }.keys.first()

        val startingPipe = Pipe(startingPosition, startingSymbol, startingConnections)

        return (pipes + startingPipe) to startingPipe
    }

    private fun parsePipe(position: Point, symbol: Char): Pipe {
        val connections = pipeTypes[symbol]!!.map { direction -> position + direction }
        return Pipe(position, symbol, connections)
    }

    data class Point(val x: Int, val y: Int) {
        operator fun plus(other: Direction) = Point(x + other.x, y + other.y)
    }

    enum class Direction(val x: Int, val y: Int) {
        L(-1, 0), R(1, 0), U(0, -1), D(0, 1)
    }

    data class Pipe(val position: Point, val symbol: Char, val connections: List<Point>)
}
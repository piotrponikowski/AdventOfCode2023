class Day3(input: List<String>) {

    private val board = input
        .flatMapIndexed { y, line -> line.mapIndexed { x, symbol -> Point(x, y) to symbol } }
        .filter { (_, symbol) -> symbol != '.' }
        .toMap()

    private val gears = board.filter { (_, symbol) -> symbol == '*' }.keys

    private val neighbours = (-1..1).flatMap { x -> (-1..1).map { y -> Point(x, y) } } - Point(0, 0)

    private val groups = parseGroups()

    fun part1() = groups
        .filter { group -> group.isPart() }
        .sumOf { group -> groupToNumber(group) }

    fun part2() = gears
        .map { gear -> groups.filter { group -> gear in group.symbols } }
        .filter { groups -> groups.size == 2 }
        .sumOf { (group1, group2) -> groupToNumber(group1) * groupToNumber(group2) }

    private fun parseGroups(): List<Group> {
        val notGroupedDigits = board.filter { (_, symbol) -> symbol.isDigit() }.keys.toMutableList()
        val groups = mutableListOf<Group>()

        while (notGroupedDigits.isNotEmpty()) {
            val pointsToCheck = mutableListOf(notGroupedDigits.removeFirst())
            val groupedPoints = mutableSetOf<Point>()
            val adjacentSymbols = mutableSetOf<Point>()

            while (pointsToCheck.isNotEmpty()) {
                val point = pointsToCheck.removeFirst()
                val symbol = board[point] ?: continue

                if (symbol.isDigit() && point !in groupedPoints) {
                    groupedPoints += point
                    pointsToCheck += neighbours.map { neighbour -> point + neighbour }
                } else if (!symbol.isDigit()) {
                    adjacentSymbols += point
                }
            }

            groups += Group(groupedPoints, adjacentSymbols)
            notGroupedDigits -= groupedPoints
        }
        return groups
    }

    private fun groupToNumber(group: Group) = group.points
        .sortedBy { point -> point.x }
        .map { point -> board[point]!! }
        .fold("") { result, current -> result + current }
        .toInt()

    data class Group(val points: Set<Point>, val symbols: Set<Point>) {
        fun isPart() = symbols.isNotEmpty()
    }

    data class Point(val x: Int, val y: Int) {
        operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    }
}
class Day2(input: List<String>) {

    private val games = parseGames(input)

    fun part1() = games.filter { game -> game.isPossible() }.sumOf { game -> game.id }

    fun part2() = games.sumOf { game -> game.power() }

    private fun parseGames(input: List<String>) = input.map { line ->
        val (gameInput, subGamesInput) = line.split(":")

        val gameId = gameInput.split(" ").last().toInt()
        val subGames = subGamesInput.split(";").map { subGameInput -> parseSubGame(subGameInput) }

        Game(gameId, subGames)
    }

    private fun parseSubGame(subGameInput: String) = subGameInput.split(",")
        .map { cubesInput -> cubesInput.trim().split(" ") }
        .map { (count, color) -> Cube(color, count.toInt()) }
        .let { cubes -> SubGame(cubes) }

    data class Game(val id: Int, val subGames: List<SubGame>) {
        fun isPossible() = subGames.all { subGame -> subGame.isPossible() }

        fun power() = subGames
            .flatMap { subGame -> subGame.cubes }
            .groupBy { cube -> cube.color }
            .map { (_, cubes) -> cubes.maxOfOrNull { cube -> cube.count }!! }
            .reduce { result, count -> result * count }
    }

    data class SubGame(val cubes: List<Cube>) {
        fun isPossible() = cubes.all { cube -> cube.isPossible() }
    }

    data class Cube(val color: String, val count: Int) {

        fun isPossible() = MAX_CUBES[color]!! >= count

        companion object {
            private val MAX_CUBES = mapOf("red" to 12, "green" to 13, "blue" to 14)
        }
    }
}
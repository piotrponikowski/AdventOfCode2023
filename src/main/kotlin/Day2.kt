import kotlin.math.max

class Day2(input: List<String>) {

    private val games = parseGames(input)

    fun part1() = games.filter { game -> game.isPossible() }.sumOf { game -> game.id }

    fun part2() = games.sumOf { game ->
        game.minCubes()
            .map { (_, count) -> count }
            .reduce { result, count -> result * count }
    }

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

        fun minCubes(): Map<String, Int> {
            val result = mutableMapOf<String, Int>()
            subGames.forEach { subGame ->
                subGame.cubes.forEach { cube ->
                    result[cube.color] = max(result[cube.color] ?: 0, cube.count)

                }
            }

            return result
        }
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

fun main() {
//    val input = readText("day2.txt", true)
    val input = readLines("day2.txt", true)

    val result = Day2(input).part1()
    println(result)
}
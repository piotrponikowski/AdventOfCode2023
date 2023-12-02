import kotlin.math.max

class Day2(val input: List<String>) {


    fun part1() {
        val test = parse()
        val test2 = test.filter { it.isPossible() }.map { it.id }.sum()
        println(test2)
    }

    fun part2() {
        val test = parse()
        val test2 = test.map { it.power() }
        println(test2.sum())
    }

    fun parse() = input.map { line ->
        val game = line.split(":").first()
        val cubes = line.split(":").last().split(";")

        val id = game.split(" ").last().toInt()

        val cubes2 = cubes.map { group ->
            group.split(",").map { it.trim() }
                .map { it.split(" ") }
                .map { (a, b) -> Cubes(b, a.toInt()) }
        }

        Game(id, cubes2)
    }


    data class Game(val id: Int, val cubes: List<List<Cubes>>) {

        fun isPossible() = cubes.all { a -> a.all { b -> colors[b.color]!! >= b.count } }

        fun power():Int {
            val maxes = mutableMapOf<String, Int>()
            cubes.forEach { a -> a.forEach { b -> maxes[b.color] = max((maxes[b.color] ?: 0), b.count) } }
            return maxes.values.reduce { a, b -> a*b}
        }

        companion object {
            val colors = mapOf("red" to 12, "green" to 13, "blue" to 14)
        }
    }

    data class Cubes(val color: String, val count: Int)
}

fun main() {
//    val input = readText("day2.txt", true)
    val input = readLines("day2.txt")

    val result = Day2(input).part2()
    println(result)
}
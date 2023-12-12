import kotlin.math.pow

class Day12(input: List<String>) {

    val springs = input.map { line -> line.split(" ") }
        .map { (state, groups) -> state to groups.split(",").map { it.toInt() } }
        .map { (state, groups) -> Spring(state, groups) }

    fun part1() = springs.map { solve(it) }.sum()

//    fun part1() = springs.map { it.solve() }.sum()
    
    fun part2() = springs.map { it.unfold() }.map { solve(it) }.sum()

    fun solve(input: Spring): Int {
        val finishedSprings = mutableListOf<Spring>()
        var springsToCheck = mutableListOf(input)
        while (springsToCheck.isNotEmpty()) {
            val toCheck = springsToCheck.removeFirst()

            if (toCheck.isSolved()) {
                finishedSprings += toCheck

            } else {
                springsToCheck.addAll(toCheck.split())
            }
        }
        
        return finishedSprings.size
    }


    data class Spring(val state: String, val groups: List<Int>) {

        fun isSolved() = state.none { it == '?' }

        fun isValid(): Boolean {
            var index = state.indexOf('?')
            if (index == -1) {
                index = state.length
            }

            val result = mutableListOf<Int>()
            var current = 0

            state.substring(0, index).forEach { symbol ->
                if (symbol == '#') {
                    current += 1
                } else if (symbol == '.') {
                    if (current > 0) {
                        result += current
                        current = 0
                    }
                }
            }

            if (current > 0 && !state.contains('?')) {
                result += current
                current = 0
            }

            if (!state.contains('?') && result.size != groups.size) {
                return false
            }

            val test = result.zip(groups).all { (a, b) -> a == b }
            return test
        }

        fun solve(result: Int = 0, input: Spring = this, mul: Int = 1): Int {
            val (a, b) = split2(input)

            var temp = result
            if (!a.isValid()) {
                temp += 0
            } else if (a.isSolved()) {
                temp += mul
            } else {
                temp += solve(result, a, mul * 2)
            }

            if (!b.isValid()) {
                temp += 0
            } else if (b.isSolved()) {
                temp += mul
            } else {
                temp += solve(result, b, mul * 2)
            }

            return temp
        }

        fun split2(input: Spring): List<Spring> {
            val index = input.state.indexOf('?')
            val result1 = input.state.substring(0, index) + '.' + input.state.substring(index + 1)
            val result2 = input.state.substring(0, index) + '#' + input.state.substring(index + 1)
            val result = listOf(result1, result2)
            return result.map { Spring(it, groups) }
        }


        fun split(): List<Spring> {
            val index = state.indexOf('?')
            val result1 = state.substring(0, index) + '.' + state.substring(index + 1)
            val result2 = state.substring(0, index) + '#' + state.substring(index + 1)
            val result = listOf(result1, result2)
            return result.map { Spring(it, groups) }.filter { it.isValid() }
        }

        fun unfold(): Spring {
            val a = (0..4).map { state }.joinToString("?")
            val b = (0..4).flatMap { groups }
            return Spring(a, b)
        }
    }
}

fun main() {
//    val input = readText("day12.txt", true)
    val input = readLines("day12.txt", true)
    val result = Day12(input).part2()
    println(result)
}
import kotlin.math.sqrt

class Day5(val input: String) {


    fun part1():Long {
        val (seeds, groups) = parse()
        var results = mutableListOf<Long>()

        seeds.forEach { seed ->
            var currentValue = seed
            groups.forEach { group ->
                currentValue = group.convert(currentValue)
            }
            println()
            results.add(currentValue)
        }
        return results.min()
    }

    fun part2() :Long {
        val (seeds, groups) = parse()
        var results = mutableListOf<Pair<Long, Long>>()
//        val groupedSeeds = seeds.windowed(2, 2)
//        val groupedSeeds = listOf<Pair<Long, Long>>(544909935L-40000 to 544909935L)
//        val groupedSeeds2 = groupedSeeds.flatMap { (a, b) -> toTest(a, b) }.toSet()

        val groupedSeeds2= ((544909935L-40000 .. 544909935L)).toList()
        
        groupedSeeds2.forEach { seed ->
            var currentValue = seed
            groups.forEach { group ->
                currentValue = group.convert(currentValue)
            }
            println("$seed -> $currentValue")
            results.add(seed to currentValue)
        }
        val results2 = results.sortedBy { it.second }

        val seed2 = results2.first().first
//        val range = groupedSeeds.find { (a, b) -> (seed2 >= a && seed2 < a + b) }

        println(seed2)
//        println(range)
        
        println(results2.take(100))
        
        //6082852
        return seed2
    }

    fun parse(): Pair<List<Long>, List<Group>> {
        val groups1 = input.split("\r\n\r\n");
        val seeds = groups1.first().split(":").last().split(" ").filter { it.isNotBlank() }.map { it.toLong() }
        val maps = groups1.drop(1)
            .map { it.split("\r\n").drop(1).map { it.split(" ").filter { it.isNotBlank() }.map { it.toLong() } } }


        val groups =
            maps.map { Group(it.map { (destination, source, range) -> Converter(destination, source, range) }) }
        return seeds to groups
    }

    fun toTest(a: Long, b: Long): List<Long> {
        val result = mutableListOf<Long>()
        val diff = sqrt(a.toDouble()).toInt()
        var start = a
        var last = a + b - 1
        var current = start

        while (current < last) {
            result += current
            current += diff
        }
        result.add(last)
        return result
    }

    data class Group(val converters: List<Converter>) {

        fun convert(value: Long): Long {
            val c = converters.find { it.matches(value) }
            if (c != null) {
                return c.convert(value)
            } else {
                return value
            }
        }
    }

    data class Converter(val destination: Long, val source: Long, val range: Long) {

        fun matches(value: Long): Boolean {
            return value in (source..<source + range)
        }

        fun convert(value: Long): Long {
            val diff = destination - source
            return value + diff
        }
    }
}

fun main() {
    val input = readText("day5.txt")
//    val input = readLines("day5.txt", true)

    val result = Day5(input).part2()
    println(result)
}
import kotlin.math.sqrt

class Day5(input: String) {

    private val seeds = parseSeeds(input)
    private val groups = parseGroups(input)

    fun part1() = seeds.minOfOrNull { seed -> processSeed(seed) }!!

    fun part2():Long {
        val bestSeed = splitSeeds()
            .map { seed -> seed to processSeed(seed) }
            .sortedBy { (_, location) -> location }
            .map { (seed, _) -> seed }
            .first()

        val scanRange = scanRange()
        val minSeed = bestSeed - scanRange
        val maxSeed = bestSeed + scanRange

        return (minSeed..maxSeed)
            .filter { seed -> validSeed(seed) }
            .minOfOrNull { seed -> processSeed(seed) }!!
    }

    private fun splitSeeds() = seeds.windowed(2, 2)
        .map { (start, range) -> start to (start + range - 1) }
        .flatMap { (start, end) -> (start..end step sqrt(start.toDouble()).toLong()).toList() + end }

    private fun validSeed(seed: Long) = seeds.windowed(2, 2)
        .any { (start, range) -> seed in (start..<start + range) }
    
    private fun scanRange() = seeds.windowed(2, 2)
        .maxOfOrNull { (start, _) -> sqrt(start.toDouble()).toLong() }!!

    private fun processSeed(seed: Long) = groups.fold(seed) { result, converter -> converter.convert(result) }

    private fun parseSeeds(input: String) = groupLines(input).first().first()
        .split(":").last().trim()
        .split(" ").map { seed -> seed.toLong() }

    private fun parseGroups(input: String) = groupLines(input).drop(1)
        .map { converters -> converters.drop(1).map { converter -> parseConverter(converter) } }
        .map { converters -> Group(converters) }

    private fun parseConverter(input: String) = input
        .split(" ")
        .map { number -> number.toLong() }
        .let { (destination, source, range) -> Converter(destination, source, range) }

    data class Group(val converters: List<Converter>) {

        fun convert(value: Long) = converters.find { converter -> converter.matches(value) }?.convert(value) ?: value
    }

    data class Converter(val destination: Long, val source: Long, val range: Long) {

        fun matches(value: Long) = value in (source..<source + range)

        fun convert(value: Long) = value + (destination - source)
    }
}
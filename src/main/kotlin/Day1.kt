class Day1(private val input: List<String>) {

    private val digits = (1..9).map { digit -> digit.toString() }
    private val numbers = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
    private val allWords = digits + numbers

    private fun findFirst(line: String, words: List<String>) = words
        .filter { digit -> line.contains(digit) }
        .map { digit -> line.indexOf(digit) to digit }
        .minBy { (index, _) -> index }
        .let { (_, digit) -> toInt(digit) }

    private fun findLast(line: String, words: List<String>) = words
        .filter { digit -> line.contains(digit) }
        .map { digit -> line.lastIndexOf(digit) to digit }
        .maxBy { (index, _) -> index }
        .let { (_, digit) -> toInt(digit) }

    private fun toInt(number: String) = when (number) {
        in digits -> number.toInt()
        else -> numbers.indexOf(number) + 1
    }

    fun part1() = input.sumOf { line -> findFirst(line, digits) * 10 + findLast(line, digits) }

    fun part2() = input.sumOf { line -> findFirst(line, allWords) * 10 + findLast(line, allWords) }
}
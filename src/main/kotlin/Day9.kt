class Day9(input: List<String>) {

    private val histories = input
        .map { line -> line.split(" ").map { number-> number.toInt() } }
        .map { numbers -> History(numbers) }

    fun part1() = histories.sumOf { history -> history.nextDiff() }

    fun part2() = histories.sumOf { history -> history.reversed().nextDiff() }

    data class History(val numbers: List<Int>) {
        fun reversed() = History(numbers.reversed())

        private fun diff() = numbers.windowed(2)
            .map { (number1, number2) -> number2 - number1 }
            .let { numbers -> History(numbers) }

        private fun isEnd() = numbers.all { number -> number == 0 }

        fun nextDiff(): Int {
            val diffs = mutableListOf(this)
            var current = this

            while (!current.isEnd()) {
                val nextDiff = current.diff()

                diffs += nextDiff
                current = nextDiff
            }

            return diffs.reversed().fold(0) { result, number -> number.numbers.last() + result }
        }
    }
}
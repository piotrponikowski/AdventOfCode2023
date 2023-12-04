class Day4(input: List<String>) {

    val cards = input.map { line -> parseCard(line) }

    fun part1() = cards.map { it.score() }.sum()

    fun part2():Int {
        var total = cards.size
        var currentCards = step(cards)
        while(currentCards.isNotEmpty()) {
            total += currentCards.size
            currentCards = step(currentCards.map { cards[it-1] })
        }
        
//        val grouped = currentCards.groupBy { it }
//        val nextCards = grouped.keys.map { cards[it-1] }
        println()
        return total
    }
    
    private fun step(currentCards: List<Card>) =currentCards.flatMap { card -> card.win() }
    

    private fun parseCard(input: String): Card {
        val (left, right) = input.split(":")
        val id = left.split(" ").last().toInt()
        val (winning, have) = right.split("|")
        val winningNumbers = winning.split(" ").filter { it.isNotBlank() }.map { it.toInt() }
        val haveNumbers = have.split(" ").filter { it.isNotBlank() }.map { it.toInt() }
        return Card(id, winningNumbers, haveNumbers)
    }

    data class Card(val id: Int, val winning: List<Int>, val have: List<Int>) {
        fun score(): Int {
            val size = winning.intersect(have).size
            if (size == 0) {
                return 0
            } else if (size == 1) {
                return 1
            } else {
                return (1..<size).fold(1) { a, b -> a * 2 }
            }
        }

        fun win(): List<Int> {
            val size = winning.intersect(have).size
            return ((id)+1..(id+size)).toList()
        }
    }

}

fun main() {
//    val input = readText("day4.txt", true)
    val input = readLines("day4.txt")

    val result = Day4(input).part2()
    println(result)
}
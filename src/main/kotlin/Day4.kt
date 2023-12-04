import kotlin.math.pow

class Day4(input: List<String>) {

    private val cards = input.map { line -> Card.parse(line) }

    fun part1() = cards.sumOf { card -> card.score() }

    fun part2(): Int {
        val scratchedCards = cards.map { card -> card.id }.associateWith { 1 }.toMutableMap()

        cards.forEach { card ->
            val cardCount = scratchedCards[card.id]!!
            card.winCards().forEach { newCardId ->
                scratchedCards.compute(newCardId) { _, scratchedCount -> (scratchedCount ?: 0) + cardCount }
            }
        }

        return scratchedCards.values.sum()
    }

    data class Card(val id: Int, val winning: Set<Int>, val have: Set<Int>) {
        fun score() = 2.0.pow(winning.intersect(have).size - 1).toInt()

        fun winCards() = ((id) + 1..(id + winning.intersect(have).size)).toList()

        companion object {
            fun parse(input: String): Card {
                val (card, winning, have) = input.split(":", "|")

                val id = card.split(" ").last().toInt()
                val winningIds = winning.split(" ").filter { it.isNotBlank() }.map { it.toInt() }
                val haveIds = have.split(" ").filter { it.isNotBlank() }.map { it.toInt() }

                return Card(id, winningIds.toSet(), haveIds.toSet())
            }
        }
    }
}

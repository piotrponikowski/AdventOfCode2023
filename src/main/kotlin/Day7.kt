class Day7(input: List<String>) {

    private val hands = input.map { it.split(" ") }.map { (a, b) -> Hand(a.toList(), b.toInt()) }
    
    fun part1() = solve(false)

    fun part2() = solve(true)
    
    private fun solve(useJokers: Boolean) = hands.sortedBy { hand -> hand.power(useJokers) }
        .mapIndexed { index, hand -> hand.bid * (index + 1) }
        .sum()


    data class Hand(val cards: List<Char>, val bid: Int) {

        fun power(useJokers: Boolean) = listOf(listOf(handPower(useJokers)), cardsPower(useJokers)).flatten()
            .joinToString { number -> number.toString().padStart(2, '0') }
        
        private fun handPower(useJoker: Boolean) = if (useJoker) handPowerJoker() else handPowerStandard()

        private fun handPowerStandard() = when {
            countGroups(5) == 1 -> 7
            countGroups(4) == 1 -> 6
            countGroups(3) == 1 && countGroups(2) == 1 -> 5
            countGroups(3) == 1 -> 4
            countGroups(2) == 2 -> 3
            countGroups(2) == 1 -> 2
            else -> 1
        }

        private fun handPowerJoker() = CARDS_POWER
            .map { replacement -> cards.map { card -> if (card == 'J') replacement else card } }
            .map { newCards -> Hand(newCards, bid) }
            .maxOfOrNull { hand -> hand.handPowerStandard() }!!
        
        private fun cardsPower(useJoker: Boolean) = if (useJoker) cardsPowerJoker() else cardsPowerStandard()
        
        private fun cardsPowerStandard() = cards.map { card -> CARDS_POWER.size - CARDS_POWER.indexOf(card) }

        private fun cardsPowerJoker() = cards.map { card -> CARDS_POWER_JOKER.size - CARDS_POWER_JOKER.indexOf(card) }

        private fun countGroups(groupSize: Int) = cards
            .groupBy { symbol -> symbol }
            .map { (_, group) -> group.size }
            .count { size -> size == groupSize }

        companion object {
            private val CARDS_POWER = arrayOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
            private val CARDS_POWER_JOKER = arrayOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')
        }
    }
}
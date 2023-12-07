class Day7(input: List<String>) {

    private val hands = input.map { it.split(" ") }.map { (a, b) -> Hand(a.toList(), b.toInt()) }

    private val cardPower = arrayOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
    private val cardPower2 = arrayOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')

    fun part1() {
        val test = hands
            .sortedWith(compareBy(
                { it.power() },
                { -cardPower.indexOf(it.cards[0]) },
                { -cardPower.indexOf(it.cards[1]) },
                { -cardPower.indexOf(it.cards[2]) },
                { -cardPower.indexOf(it.cards[3]) },
                { -cardPower.indexOf(it.cards[4]) })
            )
        
        val test2 = test.mapIndexed { index, hand -> hand.bid * (index+1) }

        test.forEach { println("$it ${it.power()} ${cardPower.indexOf(it.cards[0])} ${cardPower.indexOf(it.cards[1])}") }

        println(test2.sum())
    }

    fun part2() {
        val test = hands
            .sortedWith(compareBy(
                { it.power2() },
                { -cardPower2.indexOf(it.cards[0]) },
                { -cardPower2.indexOf(it.cards[1]) },
                { -cardPower2.indexOf(it.cards[2]) },
                { -cardPower2.indexOf(it.cards[3]) },
                { -cardPower2.indexOf(it.cards[4]) })
            )

        val test2 = test.mapIndexed { index, hand -> hand.bid * (index+1) }

        test.forEach { println("$it ${it.power2()} ${cardPower2.indexOf(it.cards[0])} ${cardPower2.indexOf(it.cards[1])}") }

        println(test2.sum())
    }


    data class Hand(val cards: List<Char>, val bid: Int) {

        fun power(cardsToCheck: List<Char> = cards): Int {
            val groups = cardsToCheck.groupBy { it }.mapValues { (a, b) -> b.size }

            if (groups.any { (key, count) -> count == 5 }) {
                return 7
            }

            if (groups.any { (key, count) -> count == 4 }) {
                return 6
            }

            if (groups.any { (key, count) -> count == 3 } && groups.any { (key, count) -> count == 2 }) {
                return 5
            }

            if (groups.any { (key, count) -> count == 3 }) {
                return 4
            }

            if (groups.count { (key, count) -> count == 2 } == 2) {
                return 3
            }

            if (groups.count { (key, count) -> count == 2 } == 1) {
                return 2
            }

            return 1
        }
        
        fun power2():Int {
            val newPowers = arrayOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
                .map { replacement -> 
                    val newCards = cards.map { if(it == 'J') replacement else it } 
                    val newPower = power(newCards)
                    newPower
                }
            
            return newPowers.max()
        }

    }
}

fun main() {
//    val input = readText("day7.txt", true)
    val input = readLines("day7.txt")
//250606082
    val result = Day7(input).part2()
    println(result)
}
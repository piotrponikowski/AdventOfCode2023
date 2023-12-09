class Day9(input: List<String>) {

    val histories= input.map { it.split(" ").map { it.toInt() } }.map { History(it) }
    
    fun part1() {
        val test = histories.map { solve(it) }
        println(test.sum())
    }

    fun part2() {
        val test = histories.map { it.reversed() }.map { solve(it) }
        println(test.sum())
    }
    
    fun solve(history: History):Int {
        val result = mutableListOf(history)
        var current = history
        
        while (!current.allZeroes()) {
            val next = current.diffs()
            
            result += next
            current = next
        }
        
        val test = result.reversed().fold(0) { a, b -> b.numbers.last() + a}
        println()
        return test
    }
    
    data class History(val numbers:List<Int>) {
        fun reversed() = History(numbers.reversed())
        
        fun diffs() = History(numbers.windowed(2).map { (a, b) -> b-a })
        
        fun allZeroes() = numbers.all { it == 0 }
        
    }
}

fun main() {
//    val input = readText("day9.txt", true)
    val input = readLines("day9.txt")

    val result = Day9(input).part2()
    println(result)
}
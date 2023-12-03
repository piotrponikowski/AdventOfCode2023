class Day4(input: String) {
    

    fun part1() = 1

    fun part2() = 2
}

fun main() {
    val input = readText("day4.txt", true)
    //val input = readLines("day4.txt", true)

    val result = Day4(input).part1()
    println(result)
}
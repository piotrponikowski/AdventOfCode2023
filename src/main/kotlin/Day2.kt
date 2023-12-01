class Day2(input: String) {
    

    fun part1() = 1

    fun part2() = 2
}

fun main() {
    val input = readText("day1.txt", true)
    //val input = readLines("day1.txt", true)

    val result = Day2(input).part1()
    println(result)
}
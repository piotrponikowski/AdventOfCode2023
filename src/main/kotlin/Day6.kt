class Day6(input: List<String>) {

    private val times = input.first().split(" ").filter { it.isNotBlank() }.drop(1).map { it.toLong() }
    private val distances = input.last().split(" ").filter { it.isNotBlank() }.drop(1).map { it.toLong() }
    private val races = times.zip(distances).map { (a, b) -> Race(a, b) }
    
    fun part1() {
        val test = races.map { it.winScenarios().size }
            .reduce { acc, i -> acc * i }
        println(test)
    }

    fun part2(time: Long, distance: Long) {
//        val toCheck  =(1..time step 100_000)
        val toCheck = (6900001L..6950001L)
        toCheck.forEach { println("$it ${isWin(time, it, distance)}") }

    }

    fun isWin(time: Long, ms: Long, record: Long): Boolean {
        return (time - ms) * ms > record
    }

    data class Race(val time: Long, val distance: Long) {

        fun winScenarios() = (1..time).map { ms -> (time - ms) * ms }.filter { it > distance }
    }
}

fun main() {
//    val input = readText("day6.txt", true)
    val input = readLines("day6.txt")

//    val result = Day6(input).part2(71530L, 940200L)
    val result = Day6(input).part2(47707566, 282107911471062L)

    println(result)
}
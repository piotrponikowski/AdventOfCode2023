class Day25(input: List<String>) {

    private val connections = input.flatMap { line -> Connection.parse(line) }.toSet()

    private val components = connections
        .groupBy { connection -> connection.name1 }
        .map { (a, b) -> a to b.map { it.name2 }.toSet() }
        .toMap()

    private val componentNames = components.keys

    fun part1() {

        val distances = componentNames.map { component -> component to calculateDistance(component) }.toMap()
        val maxDistances = distances.map { (a, b) -> a to b.map { it.distance }.max()  }

        distances.forEach { println(it) }
        println()
        val a = maxDistances.sortedBy { it.second }
        
        
        println(a)
    }

    var i = 0
    private fun calculateDistance(start: String): List<Path> {
        i++
        println(i)
        var distance = 0
        val visited = mutableListOf<String>()

        val startingPath = Path(start, start, 0)

        val toCheck = mutableListOf(startingPath)
        val result = mutableListOf(startingPath)

        while (toCheck.isNotEmpty()) {
            distance += 1
            val swap = mutableListOf<Path>()

            toCheck.forEach { current ->
                val others = components[current.end]!!

                others.forEach { newEnd ->
                    if (newEnd !in visited) {
                        val newPath = Path(start, newEnd, distance)
                        swap += newPath
                        result += newPath
                        visited += newPath.end
                    }
                }
            }
            
            toCheck.clear()
            toCheck += swap
        }

        return result
    }

    data class Path(val start: String, val end: String, val distance: Int)

    data class Connection(val name1: String, val name2: String) {

        companion object {
            fun parse(input: String): List<Connection> {
                val components = input.split(": ", " ")
                val name = components.first()
                val others = components.drop(1)

                return others.flatMap { other -> listOf(Connection(name, other), Connection(other, name)) }
            }
        }
    }
}

fun main() {
//    val input = readText("day25.txt", true)
    val input = readLines("day25.txt")

    val result = Day25(input).part1()
    println(result)
}
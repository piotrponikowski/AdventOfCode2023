class Day8(input: List<String>) {

    private val instructions = parseInstructions(input)
    private val nodes = parseNodes(input)

    fun part1(start: String = "AAA"):Int {
        var current = start
        var index = 0
        while (!current.endsWith("Z")) {
            val instruction = instructions[index % instructions.size]
            val node = nodes.find { it.start == current }!!
            if(instruction == 'L') {
                current = node.left
            } else {
                current = node.right
            }

            index += 1
        }
        return index
    }

    fun part2() {
        val staringNodes = nodes.filter { it.start.endsWith("A") }.map { it.start }
        println(staringNodes)
        
        val indices = staringNodes.map { part1(it) }.map { it.toLong() }
        println(indices)
        
        val result = indices.reduce { acc, l ->  lcm(acc, l)}
        println(result)
    }

    private fun parseInstructions(input: List<String>) = input.first().toList()
    private fun parseNodes(input: List<String>) = input.drop(2).map { line -> Node.parse(line) }

    data class Node(val start: String, val left: String, val right: String) {


        companion object {
            private val pattern = Regex("""(\w+) = \((\w+), (\w+)\)""")

            fun parse(input: String) = pattern.matchEntire(input)!!.destructured
                .let { (start, left, right) -> Node(start, left, right) }
        }
    }
}

fun main() {
//    val input = readText("day8.txt", true)
    val input = readLines("day8.txt")

    val result = Day8(input).part2()
    println(result)
}
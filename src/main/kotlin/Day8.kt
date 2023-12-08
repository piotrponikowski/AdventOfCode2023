class Day8(input: List<String>) {

    private val instructions = parseInstructions(input)
    private val nodes = parseNodes(input)

    fun part1() = solve("AAA") { current -> current != "ZZZ" }

    fun part2():Long {
        val staringNodes = nodes.filter { it.start.endsWith("A") }.map { it.start }
        println(staringNodes)

        val indices = staringNodes.map { solve(it) { current -> !current.endsWith("Z") } }.map { it.toLong() }
        println(indices)

        val result = indices.reduce { acc, l -> lcm(acc, l) }
        println(result)
        return result
    }


    fun solve(start: String = "AAA", endCondition: (String) -> Boolean): Int {
        var current = start
        var index = 0
        while (endCondition(current)) {
            val instruction = instructions[index % instructions.size]
            val node = nodes.find { it.start == current }!!
            if (instruction == 'L') {
                current = node.left
            } else {
                current = node.right
            }

            index += 1
        }
        return index
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
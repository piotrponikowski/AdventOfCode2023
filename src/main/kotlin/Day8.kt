class Day8(input: List<String>) {

    private val instructions = parseInstructions(input)
    private val nodes = parseNodes(input)

    fun part1() = solve("AAA") { current -> current != "ZZZ" }

    fun part2() = nodes.asSequence()
        .filter { node -> node.source.endsWith("A") }
        .map { node -> solve(node.source) { currentNode -> !currentNode.endsWith("Z") } }
        .reduce { result, count -> lcm(result, count) }

    fun solve(startingNode: String, endCondition: (String) -> Boolean): Long {
        var currentNode = startingNode
        var instructionIndex = 0

        while (endCondition(currentNode)) {
            val instruction = instructions[instructionIndex % instructions.size]
            val node = nodes.first { it.source == currentNode }

            currentNode = if (instruction == 'L') node.left else node.right
            instructionIndex += 1
        }

        return instructionIndex.toLong()
    }

    private fun parseInstructions(input: List<String>) = input.first().toList()
    private fun parseNodes(input: List<String>) = input.drop(2).map { line -> Node.parse(line) }

    data class Node(val source: String, val left: String, val right: String) {

        companion object {
            private val pattern = Regex("""(\w+) = \((\w+), (\w+)\)""")

            fun parse(input: String) = pattern.matchEntire(input)!!.destructured
                .let { (source, left, right) -> Node(source, left, right) }
        }
    }
}
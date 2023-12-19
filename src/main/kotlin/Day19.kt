import java.lang.IllegalArgumentException

class Day19(input: String) {

    val workflows = groupLines(input).first().map { line -> parseWorkflow(line) }
    val parts = groupLines(input).last().map { line -> parseParts(line) }
    fun part1() = 1

    fun part2() = solve2()


    fun solve2(): Long {
        val data = listOf("x", "m", "s", "a").associateWith { (1..4000) }
        val start = PartScan("in", data)

        val partsToScan = mutableListOf(start)
        val acceptedParts = mutableListOf<PartScan>()

        while (partsToScan.isNotEmpty()) {
            var part = partsToScan.removeFirst()
            println("Checking parts $part")

            val workflow = workflows.find { workflow -> workflow.name == part.target }!!

            workflow.conditions.forEach { condition ->
                val (redirected, remaining) = checkCondition(part, condition);

                if (redirected != null) {
                    if (redirected.target == "A") {
                        acceptedParts += redirected
                    } else if (redirected.target != "R") {
                        partsToScan += redirected
                    }
                }

                if (remaining != null) {
                    part = remaining
                }
            }

        }

        return acceptedParts
            .map { part -> part.elements.values }
            .sumOf { ranges -> ranges.fold(1L) { result, range -> result * range.count() } }
    }

    private fun checkCondition(part: PartScan, condition: Condition): Pair<PartScan?, PartScan?> {
        if (condition.element == null) {
            return part.withTarget(condition.target) to null
        }

        val values = part.elements[condition.element]!!
        if (condition.value!! !in values) {
            return null to part.withTarget(condition.target)
        }

        if (condition.rule == "<") {
            val redirected = part.splitDown(condition.element, condition.value).withTarget(condition.target)
            val remaining = part.splitUp(condition.element, condition.value).withTarget(condition.target)
            return redirected to remaining

        } else if (condition.rule == ">") {
            val redirected = part.splitUp(condition.element, condition.value).withTarget(condition.target)
            val remaining = part.splitDown(condition.element, condition.value).withTarget(condition.target)
            return redirected to remaining

        } else {
            throw IllegalArgumentException()
        }
    }

    private fun parseWorkflow(input: String): Workflow {

        val (name, conditionsInput) = PATTERN_WORKFLOW.matchEntire(input)!!.destructured
        val conditions = conditionsInput.split(",").map { conditionInput -> parseCondition(conditionInput) }
        return Workflow(name, conditions)
    }

    private fun parseCondition(input: String): Condition {
        return if (PATTERN_CONDITION.matches(input)) {
            val (part, rule, value, target) = PATTERN_CONDITION.matchEntire(input)!!.destructured
            Condition(part, rule, value.toInt(), target)
        } else {
            Condition(null, null, null, input)
        }
    }

    private fun parseParts(input: String): Part {
        val a = input.drop(1).dropLast(1).split(",").map { it.split("=") }
            .map { (a, b) -> a to b.toInt() }.toMap()
        return Part(a)
    }

    data class Workflow(val name: String, val conditions: List<Condition>)

    data class Condition(val element: String?, val rule: String?, val value: Int?, val target: String)

    data class Part(val data: Map<String, Int>)

    data class PartScan(val target: String, val elements: Map<String, IntRange>) {
        fun splitDown(splitElement: String, splitValue: Int) =
            withElement(splitElement, (elements[splitElement]!!.first..<splitValue))

        fun splitUp(splitElement: String, splitValue: Int) =
            withElement(splitElement, (splitValue + 1..elements[splitElement]!!.last))

        private fun withElement(replaceElement: String, newValue: IntRange) = elements
            .mapValues { (element, value) -> if (element == replaceElement) newValue else value }
            .let { newElements -> PartScan(target, newElements) }

        fun withTarget(newTarget: String) = PartScan(newTarget, elements)
    }

    companion object {
        private val PATTERN_WORKFLOW = Regex("""(\w+)\{(.*)}""")
        private val PATTERN_CONDITION = Regex("""^(\w+)([<>])(\d+):(\w+)$""")

    }
}

fun main() {
    val input = readText("day19.txt")
//    val input = readLines("day19.txt", true)

    val result = Day19(input).part2()
    println(result)
}
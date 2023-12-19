import java.lang.IllegalArgumentException

class Day19(input: List<List<String>>) {

    private val workflows = input.first().map { line -> Workflow.parse(line) }
    private val parts = input.last().map { line -> Part.parse(line) }
    private val acceptedParts = scan()

    fun part1() = parts
        .filter { part -> acceptedParts.any { partScan -> partScan.contains(part) } }
        .sumOf { part -> part.elements.values.sum() }

    fun part2() = acceptedParts
        .map { part -> part.elements.values }
        .sumOf { ranges -> ranges.fold(1L) { result, range -> result * range.count() } }

    private fun scan(): List<PartScan> {
        val startingElements = listOf("x", "m", "s", "a").associateWith { (1..4000) }
        val startingPart = PartScan("in", startingElements)

        val partsToScan = mutableListOf(startingPart)
        val acceptedParts = mutableListOf<PartScan>()

        while (partsToScan.isNotEmpty()) {
            var partScan = partsToScan.removeFirst()
            val workflow = workflows.find { workflow -> workflow.name == partScan.target }!!

            workflow.conditions.forEach { condition ->
                val (redirected, remaining) = splitByCondition(partScan, condition)

                if (redirected != null) {
                    if (redirected.target == "A") {
                        acceptedParts += redirected
                    } else if (redirected.target != "R") {
                        partsToScan += redirected
                    }
                }

                if (remaining != null) {
                    partScan = remaining
                }
            }
        }

        return acceptedParts
    }

    private fun splitByCondition(part: PartScan, condition: Condition): Pair<PartScan?, PartScan?> {
        val target = condition.target
        val splitRule = condition.rule ?: return part.withTarget(target) to null

        val splitElement = condition.element!!
        val splitValue = condition.value!!
        val elementRange = part.elements[splitElement]!!

        if (splitValue !in elementRange) {
            return null to part.withTarget(target)

        } else if (splitRule == "<") {
            val redirected = part.withElement(splitElement, elementRange.first..<splitValue)
            val remaining = part.withElement(splitElement, splitValue..elementRange.last)
            return redirected.withTarget(target) to remaining.withTarget(target)

        } else if (splitRule == ">") {
            val redirected = part.withElement(splitElement, splitValue + 1..elementRange.last)
            val remaining = part.withElement(splitElement, elementRange.first..splitValue)
            return redirected.withTarget(target) to remaining.withTarget(target)

        } else {
            throw IllegalArgumentException()
        }
    }

    data class Workflow(val name: String, val conditions: List<Condition>) {
        companion object {
            private val PATTERN_WORKFLOW = Regex("""(\w+)\{(.*)}""")

            fun parse(input: String): Workflow {
                val (name, conditionsInput) = PATTERN_WORKFLOW.matchEntire(input)!!.destructured
                val conditions = conditionsInput.split(",").map { conditionInput -> Condition.parse(conditionInput) }
                return Workflow(name, conditions)
            }
        }
    }

    data class Condition(val target: String, val element: String?, val rule: String?, val value: Int?) {
        companion object {
            private val PATTERN_CONDITION = Regex("""^(\w+)([<>])(\d+):(\w+)$""")

            fun parse(input: String): Condition {
                return if (PATTERN_CONDITION.matches(input)) {
                    val (part, rule, value, target) = PATTERN_CONDITION.matchEntire(input)!!.destructured
                    Condition(target, part, rule, value.toInt())
                } else {
                    Condition(input, null, null, null)
                }
            }
        }
    }

    data class Part(val elements: Map<String, Int>) {
        companion object {
            fun parse(input: String) = input
                .drop(1).dropLast(1).split(",")
                .map { it.split("=") }
                .associate { (a, b) -> a to b.toInt() }
                .let { elements -> Part(elements) }
        }
    }

    data class PartScan(val target: String, val elements: Map<String, IntRange>) {
        fun withElement(replace: String, newValue: IntRange) =
            PartScan(target, elements.mapValues { (element, value) -> if (element == replace) newValue else value })

        fun withTarget(newTarget: String) = PartScan(newTarget, elements)
        
        fun contains(part: Part) = part.elements.all { (element, value) -> value in elements[element]!! }
    }
}
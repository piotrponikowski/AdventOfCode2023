import java.lang.IllegalArgumentException

class Day19(input: String) {

    val workflows = groupLines(input).first().map { line -> parseWorkflow(line) }
    val parts = groupLines(input).last().map { line -> parseParts(line) }
    fun part1() {
        val a = parts.map { solve(it) }.sum()
        println(a)
    }

    fun part2() {
        return solve2()
    }

    fun solve2() {
        val data = listOf("x", "m", "s", "a").associateWith { (1..4000) }
        val start = AllParts("in", data)

        val partsToCheck = mutableListOf(start)
        val accepted = mutableListOf<AllParts>()

        while (partsToCheck.isNotEmpty()) {
            var parts = partsToCheck.removeFirst()
            println("Checking parts $parts")

            val workflow = workflows.find { it.name == parts.workflow }!!

            workflow.conditions.forEach { condition ->
                if (condition.part == null) {
                    if (condition.target == "A") {
                        accepted += parts
                    } else if (condition.target != "R") {
                        partsToCheck += AllParts(condition.target, parts.data)
                    }
                } else {
                    val values = parts.data[condition.part]!!
                    if (condition.value!! in values) {
                        if (condition.rule == "<") {
                            val redirectValues = (values.first..<condition.value)
                            val redirectData =
                                parts.data.mapValues { if (it.key == condition.part) redirectValues else it.value }

                            if (condition.target == "A") {
                                accepted += AllParts(condition.target, redirectData)
                            } else if (condition.target != "R") {
                                partsToCheck += AllParts(condition.target, redirectData)
                            }

                            val remainingValues = (condition.value..values.last)
                            val remainingData =
                                parts.data.mapValues { if (it.key == condition.part) remainingValues else it.value }
                            parts = AllParts(parts.workflow, remainingData)

                        } else if (condition.rule == ">") {

                            val redirectValues = (condition.value + 1..values.last)
                            val redirectData =
                                parts.data.mapValues { if (it.key == condition.part) redirectValues else it.value }

                            if (condition.target == "A") {
                                accepted += AllParts(condition.target, redirectData)
                            } else if (condition.target != "R") {
                                partsToCheck += AllParts(condition.target, redirectData)
                            }

                            val remainingValues = (values.first..condition.value)
                            val remainingData =
                                parts.data.mapValues { if (it.key == condition.part) remainingValues else it.value }
                            parts = AllParts(parts.workflow, remainingData)

                        } else {
                            throw IllegalArgumentException()
                        }
                    }
                }
            }
        }

        println(accepted.map { it.data.values.fold(1L) { a, b -> a * b.count() } }.sum())
    }


    fun solve(parts: Parts): Int {
        println()
        println("solving $parts")
        var currentWorkflow = "in"
        var result: String? = null

        while (result == null) {
            val workflow = workflows.find { it.name == currentWorkflow }!!
            //println("checking workflow ${workflow.name}")

            var conditionFound = false
            workflow.conditions.forEach { condition ->

                if (!conditionFound && checkCondition(parts, condition)) {
                    conditionFound = true
                    println("found ${condition.target}")
                    if (condition.target in listOf("A", "R")) {
                        result = condition.target
                    } else {
                        currentWorkflow = condition.target
                    }
                }
            }
        }

        if (result == "A") {
            return parts.data.values.sum()
        } else {
            return 0
        }
    }

    private fun checkCondition(parts: Parts, condition: Condition): Boolean {
        if (condition.part == null) {
            return true
        }

        val partValue = parts.data[condition.part]!!
        if (condition.rule == "<") {
            return partValue < condition.value!!
        } else if (condition.rule == ">") {
            return partValue > condition.value!!
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

    private fun parseParts(input: String): Parts {
        val a = input.drop(1).dropLast(1).split(",").map { it.split("=") }
            .map { (a, b) -> a to b.toInt() }.toMap()
        return Parts(a)
    }

    data class Workflow(val name: String, val conditions: List<Condition>)

    data class Condition(val part: String?, val rule: String?, val value: Int?, val target: String)

    data class Parts(val data: Map<String, Int>)

    data class AllParts(val workflow: String, val data: Map<String, IntRange>)

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
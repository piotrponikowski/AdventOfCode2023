class Day20(input: List<String>) {

    private val modules = input.map { parseModule(it) }

    fun part1() {
        solve()
    }

    fun part2() = 2

    private fun solve() {
        val flipFlopsState = modules
            .map { it.name }
            .associateWith { false }.toMutableMap()

        val multiInputs = modules
            .filter { it.type == ModuleType.CONJUNCTION }
            .associate { c -> c.name to modules.filter { m -> m.outputs.contains(c.name) }.map { it.name } }

        val results = mutableListOf<Boolean>()
        val counts = mutableMapOf(true to 0, false to 0)

        val moduleWithRX = modules.find { it.outputs.contains("rx") }!!
        val rxInputs = multiInputs[moduleWithRX.name]!!
        val rxInputValues = rxInputs.associateWith { 0L }.toMutableMap()
        
        var found = false
        (1..10000).forEach { counter ->

            val startingPulse = Pulse("broadcaster", false)
            val pulses = mutableListOf(startingPulse)
            counts[false] = counts[false]!! + 1

            while (pulses.isNotEmpty() && !found) {
                val pulse = pulses.removeFirst()
                
                val module = modules.find { it.name == pulse.module }
                if (module == null) {
                    results += pulse.type
                    
                    continue
                }

                if(pulse.module in rxInputs && !pulse.type) {
                    rxInputValues[pulse.module] = counter.toLong()
               
                    if(rxInputValues.all { it.value > 1 }) {
                        println(rxInputValues)
                        found = true
                    }
                }

                if (module.type == ModuleType.BROADCASTER) {
                    module.outputs.forEach { output ->
                        pulses += Pulse(output, pulse.type)
                        flipFlopsState[module.name] = pulse.type
                        counts[pulse.type] = counts[pulse.type]!! + 1
                    }

                } else if (module.type == ModuleType.FLIP_FLOP) {
                    if (!pulse.type) {
                        val state = flipFlopsState[module.name]!!
                        flipFlopsState[module.name] = !state

                        module.outputs.forEach { output ->
                            pulses += Pulse(output, !state)
                            counts[!state] = counts[!state]!! + 1
                        }
                    }
                } else if (module.type == ModuleType.CONJUNCTION) {
                    val inputs = multiInputs[module.name]!!
                    val allOn = inputs.all { i -> flipFlopsState[i]!! }
                    if (allOn) {
                        module.outputs.forEach { output ->
                            pulses += Pulse(output, false)
                            flipFlopsState[module.name] = false
                            counts[false] = counts[false]!! + 1
                        }
                    } else {
                        module.outputs.forEach { output ->
                            pulses += Pulse(output, true)
                            flipFlopsState[module.name] = true
                            counts[true] = counts[true]!! + 1
                        }
                    }
                }
            }
        }
        println(counts)
        println(counts[false]!! * counts[true]!!)
        
        println(rxInputValues.values.reduce { a, b -> lcm(a, b)})
    }


    private fun parseModule(input: String): Module {
        val (inputName, outputs) = input.split(" -> ")
        val outputs2 = outputs.split(", ")

        return if (inputName.startsWith("&")) {
            Module(inputName.drop(1), outputs2, ModuleType.CONJUNCTION)
        } else if (inputName.startsWith("%")) {
            Module(inputName.drop(1), outputs2, ModuleType.FLIP_FLOP)
        } else if (inputName == "broadcaster") {
            Module(inputName, outputs2, ModuleType.BROADCASTER)
        } else {
            throw IllegalArgumentException()
        }
    }

    data class Pulse(val module: String, val type: Boolean)

    enum class ModuleType {
        BROADCASTER, FLIP_FLOP, CONJUNCTION
    }

    data class Module(val name: String, val outputs: List<String>, val type: ModuleType)
}

fun main() {
//    val input = readText("day20.txt", true)
    val input = readLines("day20.txt")

    val result = Day20(input).part1()
    println(result)
}
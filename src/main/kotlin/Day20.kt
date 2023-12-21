class Day20(input: List<String>) {

    private val modules = input.map { Module.parse(it) }

    fun part1(): Long {
        val pulses = mutableListOf<Pulse>()
        val machine = Machine(modules) { pulse, _ -> pulses.add(pulse) }

        for (i in 1..1000) machine.pressButton()

        return pulses
            .groupBy { pulse -> pulse.type }.values
            .fold(1L) { result, group -> result * group.size }
    }

    fun part2(): Long {
        val pulses = mutableListOf<Pair<Pulse, Long>>()
        val machine = Machine(modules) { pulse, counter -> pulses.add(pulse to counter) }

        for (i in 1..5000) machine.pressButton()

        return machine.relevantModules()
            .map { input -> pulses.first { (pulse, _) -> pulse.moduleName == input && !pulse.type } }
            .map { (_, counter) -> counter }
            .reduce { result, counter -> lcm(result, counter) }
    }

    class Machine(private val modules: List<Module>, private val onPulse: (Pulse, Long) -> Unit) {
        private val pulses = mutableListOf<Pulse>()
        private var counter = 0L

        private val states = modules
            .map { module -> module.name }
            .associateWith { false }.toMutableMap()

        private val inputs = modules
            .map { module -> module.name }
            .associateWith { name -> modules.filter { other -> name in other.outputs }.map { other -> other.name } }

        private fun sendPulse(pulse: Pulse) {
            pulses.add(pulse)
            onPulse(pulse, counter)
        }

        fun relevantModules(): List<String> {
            val endModule = modules.first { module -> "rx" in module.outputs }
            return inputs[endModule.name]!!
        }

        fun pressButton() {
            counter += 1
            sendPulse(Pulse("broadcaster", false))

            while (pulses.isNotEmpty()) {
                val pulse = pulses.removeFirst()
                val module = modules.find { it.name == pulse.moduleName } ?: continue

                if (module.type == ModuleType.BROADCASTER) {
                    states[module.name] = pulse.type
                    module.outputs.forEach { output -> sendPulse(Pulse(output, pulse.type)) }

                } else if (module.type == ModuleType.FLIP_FLOP) {
                    if (!pulse.type) {
                        val pulseType = !states[pulse.moduleName]!!

                        states[module.name] = pulseType
                        module.outputs.forEach { output -> sendPulse(Pulse(output, pulseType)) }
                    }
                } else if (module.type == ModuleType.CONJUNCTION) {
                    val pulseType = !inputs[module.name]!!.all { input -> states[input]!! }

                    states[module.name] = pulseType
                    module.outputs.forEach { output -> sendPulse(Pulse(output, pulseType)) }
                }
            }
        }
    }

    data class Pulse(val moduleName: String, val type: Boolean)

    enum class ModuleType { BROADCASTER, FLIP_FLOP, CONJUNCTION }

    data class Module(val name: String, val outputs: List<String>, val type: ModuleType) {

        companion object {
            fun parse(input: String): Module {
                val (name, outputsData) = input.split(" -> ")
                val outputs = outputsData.split(", ")

                return if (name.startsWith("&")) {
                    Module(name.drop(1), outputs, ModuleType.CONJUNCTION)
                } else if (name.startsWith("%")) {
                    Module(name.drop(1), outputs, ModuleType.FLIP_FLOP)
                } else if (name == "broadcaster") {
                    Module(name, outputs, ModuleType.BROADCASTER)
                } else {
                    throw IllegalArgumentException()
                }
            }

        }
    }
}
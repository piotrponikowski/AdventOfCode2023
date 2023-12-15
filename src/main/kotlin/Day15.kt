class Day15(input: String) {

    private val messages = input.split(",").map { message -> Message.parse(message) }

    fun part1() = messages.sumOf { message -> hash(message.data) }

    fun part2(): Int {
        val boxes = mutableMapOf<Int, List<Message>>()
        
        messages.forEach { message ->

            val label = message.label
            val boxId = hash(message.label)
            val box = boxes[boxId] ?: emptyList()
            val exists = box.any { content -> content.label == label }

            if (message.value == null) {
                boxes[boxId] = box.filter { content -> content.label != label }
            } else if (exists) {
                boxes[boxId] = box.map { content -> if (content.label == label) message else content }
            } else {
                boxes[boxId] = box + message
            }

        }

        return boxes.map { (boxId, content) ->
            content.mapIndexed { index, message -> (boxId + 1) * (index + 1) * (message.value!!) }.sum()
        }.sum()
    }

    private fun hash(input: String) = input.toList()
        .fold(0) { result, symbol -> ((result + symbol.code) * 17) % 256 }


    data class Message(val data: String, val label: String, val value: Int? = null) {

        companion object {
            fun parse(input: String): Message {
                val (label, value) = input.split('-', '=')
                return if (value.isNotBlank()) {
                    Message(input, label, value.toInt())
                } else {
                    Message(input, label)
                }
            }
        }
    }
}

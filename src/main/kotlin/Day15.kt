class Day15(input: List<String>) {

    val messages = input.first().split(",")
    
    fun part1() {
        val a = messages.map { box(it) }.sum()
        println(a)
    }
  
    fun part2() :Int{
        val registers = mutableMapOf<Int, List<Lens>>()
        messages.forEach { line ->
            val label = split(line)
            val boxId = box(label)
            
            if(line.last() == '-') {
                val lenses = registers[boxId] ?: listOf()
                val newLenses = lenses.filter { it.label != label }
                registers[boxId] = newLenses
            } else {
                val lenses = registers[boxId] ?: listOf()
                val containLenses = lenses.any { it.label == label }
                if(containLenses) {
                    val newLenses = lenses.map { if(it.label == label) Lens(it.label, line.split("=").last().toInt()) else it }
                    registers[boxId] = newLenses
                } else {
                    val newLenses = lenses + Lens(label, line.split("=").last().toInt())
                    registers[boxId] = newLenses  
                }
       
            }

            println()
            println(line)
            println(registers)
        }
        
        
        val result = registers.map { (boxId, lenses) ->
            lenses.mapIndexed { index, lens -> 
                val a = boxId + 1
                val b = index + 1
                val c = lens.value
                a * b * c
            }.sum()
        }.sum()
      return result
    }
    
    private fun split(input: String):String {
        if(input.last() == '-') {
            return input.dropLast(1)
        } else {
            return input.split("=").first() 
        }
    }
    
    private fun box(input:String):Int {
        var result = 0
        input.toList().forEach { symbol ->
            result += symbol.code
            result *= 17
            result %= 256
        }
        return result
    }
    
    data class Lens(val label:String, val value: Int)

}

fun main() {
//    val input = readText("day15.txt", true)
    val input = readLines("day15.txt")

    val result = Day15(input).part2()
    println(result)
}
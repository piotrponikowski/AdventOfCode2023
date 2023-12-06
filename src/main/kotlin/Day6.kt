import kotlin.math.sqrt

class Day6(input: List<String>) {

    private val times = parseLine(input.first())
    private val distances = parseLine(input.last())

    fun part1() = times.zip(distances)
        .map { (raceTime, distance) -> (1..raceTime).filter { holdTime -> isWin(raceTime, holdTime, distance) }.size }
        .reduce { result, winCount -> result * winCount }

    fun part2(): Long {
        val raceTime = mergeNumbers(times)
        val distance = mergeNumbers(distances)

        val scanStep = sqrt(raceTime.toDouble()).toLong()
        val stepsToScan = (1..raceTime step scanStep)

        val scanMinStep = stepsToScan.first { holdTime -> isWin(raceTime, holdTime, distance) }
        val scanMaxStep = stepsToScan.last { holdTime -> isWin(raceTime, holdTime, distance) }

        val minStep = (scanMinStep - scanStep..scanMinStep).first { holdTime -> isWin(raceTime, holdTime, distance) }
        val maxStep = (scanMaxStep..scanMaxStep + scanStep).last { holdTime -> isWin(raceTime, holdTime, distance) }

        return maxStep - minStep + 1
    }

    private fun parseLine(line: String) = line.split(" ")
        .filter { part -> part.isNotBlank() }.drop(1)
        .map { part -> part.toLong() }

    private fun mergeNumbers(numbers: List<Long>) = numbers.fold("") { result, number -> result + number }.toLong()

    private fun isWin(raceTime: Long, holdTime: Long, distance: Long) = (raceTime - holdTime) * holdTime > distance

}
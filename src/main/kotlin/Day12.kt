class Day12(input: List<String>) {

    private val data = input.map { line -> line.split(" ") }
        .map { (state, groups) -> state to groups.split(",").map { it.toInt() } }
        .map { (state, groups) -> ConditionData(state, groups) }

    fun part1() = data.sumOf { conditionData -> count(conditionData.withEnd()) }

    fun part2() = data.sumOf { conditionData -> count(conditionData.unfold(5).withEnd()) }

    private val cache = mutableMapOf<CacheKey, Long>()

    private fun count(condition: ConditionData, position: Int = 0, count: Int = 0, group: Int = 0): Long {

        val cacheKey = CacheKey(condition, position, count, group)
        if (cache.containsKey(cacheKey)) {
            return cache[cacheKey]!!
        }

        var result = 0L
        if (position == condition.data.length) {
            result = if (condition.groups.size == group) 1 else 0

        } else if (condition.data[position] == '#') {
            result += countDamaged(condition, position, count, group)

        } else if (condition.data[position] == '.') {
            result += countOperational(condition, position, count, group)

        } else if (condition.data[position] == '?') {
            result += countDamaged(condition, position, count, group)
            result += countOperational(condition, position, count, group)
        }

        cache[cacheKey] = result
        return result
    }

    private fun countDamaged(condition: ConditionData, position: Int, count: Int, group: Int): Long {
        return count(condition, position + 1, count + 1, group)
    }

    private fun countOperational(condition: ConditionData, position: Int, count: Int, group: Int): Long {
        return if (group < condition.groups.size && count == condition.groups[group]) {
            count(condition, position + 1, 0, group + 1)
        } else if (count == 0) {
            count(condition, position + 1, 0, group)
        } else {
            0
        }
    }

    data class ConditionData(val data: String, val groups: List<Int>) {

        fun unfold(repeat: Int): ConditionData {
            val newData = (0..<repeat).joinToString("?") { data }
            val newGroups = (0..<repeat).flatMap { groups }
            return ConditionData(newData, newGroups)
        }

        fun withEnd() = ConditionData("$data.", groups)
    }

    data class CacheKey(val condition: ConditionData, val position: Int, val count: Int, val group: Int)
}
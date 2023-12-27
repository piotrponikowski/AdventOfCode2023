import java.lang.IllegalStateException

class Day25(input: List<String>) {

    private val connections = input.flatMap { line -> Connection.parse(line) }.toSet()

    private val moduleNames = connections.flatMap { connection -> connection.modules }.toSet()

    fun part1(): Int {
        val connectionCounter = mutableMapOf<Connection, Int>()
        val modules = calculateModules(connections)

        (1..10000).forEach { _ ->
            val path = randomPath(modules)
            val connections = path.modules.windowed(2)
                .map { (pathName1, pathName2) -> Connection(setOf(pathName1, pathName2)) }

            connections.forEach { connection ->
                connectionCounter[connection] = (connectionCounter[connection] ?: 0) + 1
            }
        }

        val connectionsToRemove = connectionCounter.toList()
            .sortedBy { (_, count) -> -count }.take(3)
            .map { (connection, _) -> connection }.toSet()

        val newConnections = connections - connectionsToRemove
        val newModules = calculateModules(newConnections)

        return calculateSize(newConnections, newModules)
    }

    private fun calculateSize(connections: Set<Connection>, modules: Map<String, List<String>>): Int {
        val startModule = connections.first().modules.first()

        val modulesToCheck = mutableListOf(startModule)
        val visited = mutableSetOf(startModule)

        while (modulesToCheck.isNotEmpty()) {
            val module = modulesToCheck.removeFirst()

            val nextModules = modules[module]!!
            nextModules.forEach { nextModule ->
                if (nextModule !in visited) {
                    modulesToCheck += nextModule
                    visited += nextModule
                }
            }
        }

        val visitedCount = visited.size
        val notVisitedCount = modules.keys.size - visitedCount

        return visitedCount * notVisitedCount
    }

    private fun randomPath(modules: Map<String, List<String>>): Path {
        val moduleName1 = moduleNames.random()
        val moduleName2 = moduleNames.filter { moduleName -> moduleName != moduleName1 }.random()

        return calculatePath(moduleName1, moduleName2, modules)
    }

    private fun calculatePath(start: String, end: String, modules: Map<String, List<String>>): Path {
        val paths = mutableListOf(Path(listOf(start)))
        val visited = mutableSetOf(start)

        while (paths.isNotEmpty()) {
            val path = paths.removeFirst()

            val nextModules = modules[path.modules.last()]!!
            nextModules.forEach { nextModule ->
                if (nextModule !in visited) {
                    visited += nextModule
                    val nextPath = Path(path.modules + nextModule)

                    if (nextModule == end) {
                        return nextPath
                    } else {
                        paths += nextPath
                    }
                }
            }
        }

        throw IllegalStateException()
    }

    private fun calculateModules(connections: Set<Connection>) = moduleNames
        .associateWith { moduleName ->
            connections
                .filter { connection -> moduleName in connection.modules }
                .flatMap { connection -> connection.modules }
                .filter { otherName -> otherName != moduleName }
        }

    data class Path(val modules: List<String>)

    data class Connection(val modules: Set<String>) {

        companion object {
            fun parse(input: String): List<Connection> {
                val components = input.split(": ", " ")
                val name = components.first()
                val others = components.drop(1)

                return others.map { other -> Connection(setOf(name, other)) }
            }
        }
    }
}
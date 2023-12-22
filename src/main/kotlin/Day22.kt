import kotlin.math.min

class Day22(input: List<String>) {

    private val bricks = input.map { line -> line.split("~") }
        .map { points ->
            points.map { point -> point.split(",").map { it.toLong() } }
                .map { points2 -> points2.let { (a, b, c) -> Point(a, b, c) } }
        }
        .mapIndexed { index, (a, b) -> Brick(index + 1, a, b) }


    fun part1() {
        solve()
        println()
    }

    fun part2() = 2

    fun solve() {
        var state = bricks.toList()
        val ids = bricks.map { it.id }

        var changed = true
        while (changed) {
            changed = false

            println()
            println("#################")

            ids.forEach { id ->
         
            
                val brick = state.find { it.id == id }!!
                val minZ = brick.minZ()
                val checkZ = minZ - 1

                if (checkZ > 0) {
                    val rangeX = brick.rangeX
                    val rangeY = brick.rangeY
                    val area = rangeX.count() *  rangeY.count()

                    val emptyBelow = rangeY.sumOf { y ->
                        rangeX.count { x ->
                            state.none { other -> other.id != id && other.contain(Point(x, y, checkZ)) }
                        }
                    }

                    if (emptyBelow == area) {
                        //println("Falling $brick")
                        state = state.map { b -> if (b.id == id) b.fall() else b }
                        changed = true
                    } else {
                        //println("Not falling $brick")
                    }
                }
            }
        }

        var result = 0
        val supportedBy = state.map { brick ->
            val rangeX = brick.rangeX
            val rangeY = brick.rangeY
            
            val minZ = brick.minZ()
            val checkZ = minZ - 1

            val bricksBelow = rangeY.flatMap { y ->
                rangeX.flatMap { x ->
                    state.filter { other -> other.id != brick.id && other.contain(Point(x, y, checkZ)) }
                }
            }.toSet()
            
            brick.id to bricksBelow.map { it.id }
        }
        
        val canRemove = bricks.filter { brick ->
            val neededFor = supportedBy.filter { brick.id in it.second }
            neededFor.all { other -> other.second.size > 1 }
        }
        
        println(canRemove.size)
    }

    data class Brick(val id: Int, val start: Point, val end: Point) {

        val rangeX = if (end.x < start.x) end.x..start.x else start.x..end.x
        val rangeY = if (end.y < start.y) end.y..start.y else start.y..end.y
        val rangeZ = if (end.z < start.z) end.z..start.z else start.z..end.z


        fun minZ() = min(start.z, end.z)

        fun maxZ() = min(start.z, end.z)

        fun contain(point: Point) = point.x in rangeX && point.y in rangeY && point.z in rangeZ
        fun fall() = Brick(id, Point(start.x, start.y, start.z - 1), Point(end.x, end.y, end.z - 1))

    }

    data class Point(val x: Long, val y: Long, val z: Long) {
        operator fun plus(other: Point) = Point(x + other.x, y + other.y, z + other.z)
    }
}

fun main() {
//    val input = readText("day22.txt", true)
    val input = readLines("day22.txt")

    val result = Day22(input).part1()
    println(result)
}
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Day20Test : FunSpec({

    val realInput = readLines("day20.txt")
    val exampleInput1 = readLines("day20-1.txt", true)
    val exampleInput2 = readLines("day20-2.txt", true)

    context("Part 1") {
        test("should solve example 1") {
            Day20(exampleInput1).part1() shouldBe 32000000
        }
        
        test("should solve example 2") {
            Day20(exampleInput2).part1() shouldBe 11687500
        }

        test("should solve real input") {
            Day20(realInput).part1() shouldBe 944750144
        }
    }

    context("Part 2") {
        
        test("should solve real input") {
            Day20(realInput).part2() shouldBe 222718819437131
        }
    }
})

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Day23Test : FunSpec({

    val realInput = readLines("day23.txt")
    val exampleInput = readLines("day23.txt", true)

    context("Part 1") {
        test("should solve example 1") {
            Day23(exampleInput).part1() shouldBe 94
        }

        test("should solve real input") {
            Day23(realInput).part1() shouldBe 2070
        }
    }

    context("Part 2") {
        test("should solve example 1") {
            Day23(exampleInput).part2() shouldBe 154
        }
        
        test("should solve real input") {
            Day23(realInput).part2() shouldBe 6498
        }
    }
})

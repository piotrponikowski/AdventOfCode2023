import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Day18Test : FunSpec({

    val realInput = readLines("day18.txt")
    val exampleInput = readLines("day18.txt", true)


    context("Part 1") {
        test("should solve example") {
            Day18(exampleInput).part1() shouldBe 62
        }

        test("should solve real input") {
            Day18(realInput).part1() shouldBe 95356
        }
    }

    context("Part 2") {
        test("should solve example") {
            Day18(exampleInput).part2() shouldBe 952408144115
        }
        
        test("should solve real input") {
            Day18(realInput).part2() shouldBe 92291468914147
        }
    }
})

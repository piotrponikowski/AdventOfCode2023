import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Day10Test : FunSpec({

    val realInput = readLines("day10.txt")
    val exampleInput1 = readLines("day10-1.txt", true)
    val exampleInput2 = readLines("day10-2.txt", true)

    context("Part 1") {
        test("should solve example") {
            Day10(exampleInput1).part1() shouldBe 8
        }

        test("should solve real input") {
            Day10(realInput).part1() shouldBe 6828
        }
    }

    context("Part 2") {
        test("should solve example") {
            Day10(exampleInput2).part2() shouldBe 10
        }
        
        test("should solve real input") {
            Day10(realInput).part2() shouldBe 459
        }
    }
})

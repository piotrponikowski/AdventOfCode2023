import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Day11Test : FunSpec({

    val realInput = readLines("day11.txt")
    val exampleInput = readLines("day11.txt", true)

    context("Part 1") {
        test("should solve example") {
            Day11(exampleInput).part1() shouldBe 374L
        }

        test("should solve real input") {
            Day11(realInput).part1() shouldBe 9233514L
        }
    }

    context("Part 2") {
        test("should solve example") {
            Day11(exampleInput).part2() shouldBe 82000210L
        }
        
        test("should solve real input") {
            Day11(realInput).part2() shouldBe 363293506944
        }
    }
})

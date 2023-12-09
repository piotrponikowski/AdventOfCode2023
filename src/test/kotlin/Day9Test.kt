import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Day9Test : FunSpec({

    val realInput = readLines("day9.txt")
    val exampleInput = readLines("day9.txt", true)
    
    context("Part 1") {
        test("should solve example") {
            Day9(exampleInput).part1() shouldBe 114
        }

        test("should solve real input") {
            Day9(realInput).part1() shouldBe 1980437560
        }
    }

    context("Part 2") {
        test("should solve example") {
            Day9(exampleInput).part2() shouldBe 2
        }
        
        test("should solve real input") {
            Day9(realInput).part2() shouldBe 977
        }
    }
})

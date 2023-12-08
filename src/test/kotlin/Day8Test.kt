import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Day8Test : FunSpec({

    val realInput = readLines("day8.txt")
    val exampleInput1 = readLines("day8-1.txt", true)
    val exampleInput2 = readLines("day8-2.txt", true)
    val exampleInput3 = readLines("day8-3.txt", true)
    
    context("Part 1") {
        test("should solve example 1") {
            Day8(exampleInput1).part1() shouldBe 2
        }

        test("should solve example 2") {
            Day8(exampleInput2).part1() shouldBe 6
        }


        test("should solve real input") {
            Day8(realInput).part1() shouldBe 16043
        }
    }

    context("Part 2") {
        test("should solve example") {
            Day8(exampleInput3).part2() shouldBe 6
        }
        
        test("should solve real input") {
            Day8(realInput).part2() shouldBe 15726453850399
        }
    }
})

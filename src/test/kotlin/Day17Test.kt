import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Day17Test : FunSpec({

    val realInput = readLines("day17.txt")
    val exampleInput1 = readLines("day17-1.txt", true)
    val exampleInput2 = readLines("day17-2.txt", true)


    context("Part 1") {
        test("should solve example") {
            Day17(exampleInput1).part1() shouldBe 102
        }

        test("should solve real input") {
            Day17(realInput).part1() shouldBe 1065
        }
    }

    context("Part 2") {
        test("should solve example 1") {
            Day17(exampleInput1).part2() shouldBe 94
        }

        test("should solve example 2") {
            Day17(exampleInput2).part2() shouldBe 71
        }
        
        test("should solve real input") {
            Day17(realInput).part2() shouldBe 1249
        }
    }
})

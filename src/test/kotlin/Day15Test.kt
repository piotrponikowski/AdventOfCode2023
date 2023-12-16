import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Day15Test : FunSpec({

    val realInput = readText("day15.txt")
    val exampleInput = readText("day15.txt", true)

    context("Part 1") {
        test("should solve example") {
            Day15(exampleInput).part1() shouldBe 1320
        }

        test("should solve real input") {
            Day15(realInput).part1() shouldBe 495972
        }
    }

    context("Part 2") {
        test("should solve example") {
            Day15(exampleInput).part2() shouldBe 145
        }
        
        test("should solve real input") {
            Day15(realInput).part2() shouldBe 245223
        }
    }
})

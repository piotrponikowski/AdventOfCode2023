import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Day13Test : FunSpec({

    val realInput = readText("day13.txt")
    val exampleInput = readText("day13.txt", true)

    context("Part 1") {
        test("should solve example") {
            Day13(exampleInput).part1() shouldBe 405
        }

        test("should solve real input") {
            Day13(realInput).part1() shouldBe 27300
        }
    }

    context("Part 2") {
        test("should solve example") {
            Day13(exampleInput).part2() shouldBe 400
        }
        
        test("should solve real input") {
            Day13(realInput).part2() shouldBe 29276
        }
    }
})

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Day19Test : FunSpec({

    val realInput = readText("day19.txt")
    val exampleInput = readText("day19.txt", true)


    context("Part 1") {
        test("should solve example") {
            Day19(exampleInput).part1() shouldBe 19114
        }

        test("should solve real input") {
            Day19(realInput).part1() shouldBe 263678
        }
    }

    context("Part 2") {
        test("should solve example") {
            Day19(exampleInput).part2() shouldBe 167409079868000
        }
        
        test("should solve real input") {
            Day19(realInput).part2() shouldBe 125455345557345
        }
    }
})

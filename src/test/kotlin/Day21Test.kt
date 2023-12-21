import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Day21Test : FunSpec({

    val realInput = readLines("day21.txt")
    val exampleInput = readLines("day21.txt", true)

    context("Part 1") {
        test("should solve example") {
            Day21(exampleInput).part1() shouldBe 32000000
        }

        test("should solve real input") {
            Day21(realInput).part1() shouldBe 3764
        }
    }

    context("Part 2") {
        
        test("should solve real input") {
            Day21(realInput).part2() shouldBe 622926941971282
        }
    }
})

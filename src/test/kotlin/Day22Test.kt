import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Day22Test : FunSpec({

    val realInput = readLines("day22.txt")
    val exampleInput = readLines("day22.txt", true)

    context("Part 1") {
        test("should solve example 1") {
            Day22(exampleInput).part1() shouldBe 5
        }

        test("should solve real input") {
            Day22(realInput).part1() shouldBe 375
        }
    }

    context("Part 2") {
        test("should solve example 1") {
            Day22(exampleInput).part2() shouldBe 7
        }
        
        test("should solve real input") {
            Day22(realInput).part2() shouldBe 72352
        }
    }
})

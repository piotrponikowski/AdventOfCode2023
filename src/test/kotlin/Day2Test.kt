import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Day2Test : FunSpec({

    val realInput = readLines("day2.txt")
    val exampleInput = readLines("day2.txt", true)
    
    context("Part 1") {
        test("should solve example") {
            Day2(exampleInput).part1() shouldBe 8
        }
        
        test("should solve real input") {
            Day2(realInput).part1() shouldBe 2810
        }
    }

    context("Part 2") {
        test("should solve example") {
            Day2(exampleInput).part2() shouldBe 2286
        }
        
        test("should solve real input") {
            Day2(realInput).part2() shouldBe 69110
        }
    }
})

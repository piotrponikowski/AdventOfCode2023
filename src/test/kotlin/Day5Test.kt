import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Day5Test : FunSpec({

    val realInput = readText("day5.txt")
    val exampleInput = readText("day5.txt", true)
    
    context("Part 1") {
        test("should solve example") {
            Day5(exampleInput).part1() shouldBe 35
        }
        
        test("should solve real input") {
            Day5(realInput).part1() shouldBe 3374647
        }
    }

    context("Part 2") {
        test("should solve example") {
            Day5(exampleInput).part2() shouldBe 46
        }
        
        test("should solve real input") {
            Day5(realInput).part2() shouldBe 6082852
        }
    }
})

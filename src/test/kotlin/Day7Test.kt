import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Day7Test : FunSpec({

    val realInput = readLines("day7.txt")
    val exampleInput = readLines("day7.txt", true)
    
    context("Part 1") {
        test("should solve example") {
            Day7(exampleInput).part1() shouldBe 6440
        }
        
        test("should solve real input") {
            Day7(realInput).part1() shouldBe 250254244
        }
    }

    context("Part 2") {
        test("should solve example") {
            Day7(exampleInput).part2() shouldBe 5905
        }
        
        test("should solve real input") {
            Day7(realInput).part2() shouldBe 250087440
        }
    }
})

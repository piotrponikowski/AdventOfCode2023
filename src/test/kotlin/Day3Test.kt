import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Day3Test : FunSpec({

    val realInput = readLines("day3.txt")
    val exampleInput = readLines("day3.txt", true)
    
    context("Part 1") {
        test("should solve example") {
            Day3(exampleInput).part1() shouldBe 4361
        }
        
        test("should solve real input") {
            Day3(realInput).part1() shouldBe 525911
        }
    }

    context("Part 2") {
        test("should solve example") {
            Day3(exampleInput).part2() shouldBe 467835
        }
        
        test("should solve real input") {
            Day3(realInput).part2() shouldBe 75805607
        }
    }
})

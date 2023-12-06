import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Day6Test : FunSpec({

    val realInput = readLines("day6.txt")
    val exampleInput = readLines("day6.txt", true)
    
    context("Part 1") {
        test("should solve example") {
            Day6(exampleInput).part1() shouldBe 288L
        }
        
        test("should solve real input") {
            Day6(realInput).part1() shouldBe 281600L
        }
    }

    context("Part 2") {
        test("should solve example") {
            Day6(exampleInput).part2() shouldBe 71503L
        }
        
        test("should solve real input") {
            Day6(realInput).part2() shouldBe 33875953L
        }
    }
})

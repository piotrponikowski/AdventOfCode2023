import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Day1Test : FunSpec({

    val realInput = readLines("day1.txt")
    val exampleInput1 = readLines("day1-1.txt", true)
    val exampleInput2 = readLines("day1-2.txt", true)
    
    context("Part 1") {
        test("should solve example") {
            Day1(exampleInput1).part1() shouldBe 142
        }
        
        test("should solve real input") {
            Day1(realInput).part1() shouldBe 56506
        }
    }

    context("Part 2") {
        test("should solve example") {
            Day1(exampleInput2).part2() shouldBe 281
        }
        
        test("should solve real input") {
            Day1(realInput).part2() shouldBe 56017
        }
    }
})

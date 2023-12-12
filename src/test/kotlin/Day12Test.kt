import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Day12Test : FunSpec({

    val realInput = readLines("day12.txt")
    val exampleInput = readLines("day12.txt", true)

    context("Part 1") {
        test("should solve example") {
            Day12(exampleInput).part1() shouldBe 21
        }

        test("should solve real input") {
            Day12(realInput).part1() shouldBe 6827
        }
    }

    context("Part 2") {
        test("should solve example") {
            Day12(exampleInput).part2() shouldBe 525152
        }
        
        test("should solve real input") {
            Day12(realInput).part2() shouldBe 1537505634471
        }
    }
})

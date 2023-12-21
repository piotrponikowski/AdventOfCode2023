import io.kotest.core.spec.style.FunSpec
import io.kotest.data.forAll
import io.kotest.data.headers
import io.kotest.data.row
import io.kotest.data.table
import io.kotest.matchers.shouldBe

class Day21Test : FunSpec({

    val realInput = readLines("day21.txt")
    val exampleInput = readLines("day21.txt", true)

    context("Part 1") {
        test("should solve example") {
            Day21(exampleInput).part1() shouldBe 2665
        }

        test("should solve real input") {
            Day21(realInput).part1() shouldBe 3764
        }
    }

    context("Part 2") {

        context("should solve example") {
            table(
                headers("input", "result"),
                row(6, 16),
                row(10, 50),
                row(50, 1594),
                row(100, 6536),
            ).forAll { steps, result ->
                Day21(exampleInput).solve(steps) shouldBe result
            }
        }

        test("should solve real input") {
            Day21(realInput).part2() shouldBe 622926941971282
        }
    }
})

import Result.*
import Sign.*

sealed class Result(val score: Int) {
    object Win : Result(6)
    object Draw : Result(3)
    object Lose : Result(0)
}

sealed class Sign(val score: Int) {
    abstract fun vs(sign: Sign): Result
    abstract fun simulate(result: Result): Sign

    object Rock : Sign(1) {
        override fun vs(sign: Sign) = when (sign) {
            is Rock -> Draw
            is Paper -> Lose
            is Scissors -> Win
        }

        override fun simulate(result: Result) = when (result) {
            is Draw -> Rock
            is Lose -> Scissors
            is Win -> Paper
        }
    }

    object Paper : Sign(2) {
        override fun vs(sign: Sign) = when (sign) {
            is Rock -> Win
            is Paper -> Draw
            is Scissors -> Lose
        }

        override fun simulate(result: Result) = when (result) {
            is Draw -> Paper
            is Lose -> Rock
            is Win -> Scissors
        }
    }

    object Scissors : Sign(3) {
        override fun vs(sign: Sign) = when (sign) {
            is Rock -> Lose
            is Paper -> Win
            is Scissors -> Draw
        }

        override fun simulate(result: Result) = when (result) {
            is Draw -> Scissors
            is Lose -> Paper
            is Win -> Rock
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        fun toSign(sign: String): Sign = when (sign) {
            "A", "X" -> Rock
            "B", "Y" -> Paper
            "C", "Z" -> Scissors
            else -> throw Error("Not supported sign [$sign]")
        }

        return input.map { line ->
            val pair = line.split(" ")
            toSign(pair.first()) to toSign(pair.last())
        }.sumOf { pair ->
            val opponent = pair.first
            val yours = pair.second
            yours.score + yours.vs(opponent).score
        }
    }

    fun part2(input: List<String>): Int {

        fun toSign(sign: String): Sign = when (sign) {
            "A" -> Rock
            "B" -> Paper
            "C" -> Scissors
            else -> throw Error("Not supported sign [$sign]")
        }

        fun toResult(sign: String): Result = when (sign) {
            "X" -> Lose
            "Y" -> Draw
            "Z" -> Win
            else -> throw Error("Not supported sign [$sign]")
        }

        return input.map { line ->
            val pair = line.split(" ")
            toSign(pair.first()) to toResult(pair.last())
        }.sumOf { pair ->
            val opponent = pair.first
            val result = pair.second
            val yours = opponent.simulate(result)
            yours.score + result.score
        }
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}

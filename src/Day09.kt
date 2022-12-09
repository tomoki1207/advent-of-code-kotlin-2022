import Motion.*
import kotlin.math.abs
import kotlin.math.sign

enum class Motion {
    Up, Down, Left, Right;

    companion object {
        fun from(from: String) = when (from) {
            "U" -> Up
            "D" -> Down
            "L" -> Left
            "R" -> Right
            else -> throw Error("Unknown motion $from")
        }
    }
}

data class Knot(val row: Int, val col: Int)

data class Rope(val knotCount: Int) {
    private var head = Knot(0, 0)
    private var knots = List(knotCount) { Knot(0, 0) }

    fun step(motion: Motion, callbackWithTail: (Knot) -> Unit) {
        var forward = nextHead(motion)

        head = forward
        knots = knots.map { k ->
            val diffRow = forward.row - k.row
            val diffCol = forward.col - k.col

            if (abs(diffRow) <= 1 && abs(diffCol) <= 1) {
                forward = k
                return@map k
            }

            val moved = Knot(k.row + diffRow.sign, k.col + diffCol.sign)
            forward = moved
            moved
        }
        callbackWithTail(knots.last())
    }

    private fun nextHead(motion: Motion) = when (motion) {
        Up -> Knot(head.row + 1, head.col)
        Down -> Knot(head.row - 1, head.col)
        Left -> Knot(head.row, head.col - 1)
        Right -> Knot(head.row, head.col + 1)
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        val steps = input.map { step -> step.split(" ").let { Pair(Motion.from(it[0]), it[1].toInt()) } }

        val rope = Rope(1)
        val tailHistory = mutableSetOf<Knot>()
        steps.forEach { step ->
            repeat(step.second) {
                rope.step(step.first) {
                    tailHistory.add(it)
                }
            }
        }
        return tailHistory.size
    }

    fun part2(input: List<String>): Int {
        val steps = input.map { step -> step.split(" ").let { Pair(Motion.from(it[0]), it[1].toInt()) } }

        val rope = Rope(9)
        val tailHistory = mutableSetOf<Knot>()
        steps.forEach { step ->
            repeat(step.second) {
                rope.step(step.first) {
                    tailHistory.add(it)
                }
            }
        }
        return tailHistory.size
    }

    check(part1(readInput("Day09_test")) == 13)
    check(part2(readInput("Day09_test")) == 1)
    check(part2(readInput("Day09_test2")) == 36)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}

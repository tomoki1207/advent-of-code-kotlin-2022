data class Cpu(
    var x: Int = 1,
    var cycle: Int = 0
) {
    val signalStrengths = mutableListOf<Int>()
    fun exec(instruction: String, arg: Int?) {
        when (instruction) {
            "noop" -> {
                cycle++
                signalStrengths.add(x * cycle)
                draw()
            }

            "addx" -> {
                repeat(2) {
                    cycle++
                    signalStrengths.add(x * cycle)
                    draw()
                }
                x += arg!!
            }
        }
    }

    private fun draw() {
        print(if (inSprite()) "#" else ".")
        if (cycle % 40 == 0) {
            println()
        }
    }

    private fun inSprite(): Boolean {
        val col = cycle % 40
        return col in x..x + 2
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        val cpu = Cpu()
        input.map { line -> line.split(" ").let { Pair(it[0], if (it.size == 2) it[1].toInt() else null) } }
            .forEach {
                cpu.exec(it.first, it.second)
            }
        return cpu.signalStrengths.filterIndexed { index, _ -> ((index + 1) - 20) % 40 == 0 }.sum()
    }

    fun part2(){
        println("Check above!!")
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13140)

    val input = readInput("Day10")
    println(part1(input))
    part2()
}

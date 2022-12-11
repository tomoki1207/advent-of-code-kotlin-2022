import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

data class Monkey(val specs: List<String>) {
    private val engine: ScriptEngine = ScriptEngineManager().getEngineByExtension("js")
    val no: Int = "\\d+".toRegex().find(specs[0])!!.value.toInt()
    val items: MutableList<Int> = specs[1].substringAfter("items: ").split(", ").map { it.toInt() }.toMutableList()
    private val operation: String = specs[2].substringAfter("= ")
    val divisible: Int = specs[3].substringAfter("by ").toInt()
    private val throwToWhenTrue: Int = specs[4].substringAfter("monkey ").toInt()
    private val throwToWhenFalse: Int = specs[5].substringAfter("monkey ").toInt()
    var times: Int = 0

    fun turn(reducer: (Long) -> Long): List<Pair<Int, Int>> {
        if (items.isEmpty()) {
            return emptyList()
        }
        val throwTo = items.map { item ->
            times++
            val level = reducer((engine.eval(operation.replace("old", item.toString())) as Number).toLong())
            val mod = (level % divisible).toInt()
            (if (mod == 0) throwToWhenTrue else throwToWhenFalse) to level.toInt()
        }

        items.clear()
        return throwTo
    }
}

fun main() {

    fun part1(input: List<String>): Long {
        val monkeys = input.chunked(7).map { Monkey(it) }.associateBy { it.no }
        repeat(20) {
            for (monkey in monkeys.values) {
                monkey.turn { it / 3 }.map { (no, level) ->
                    monkeys[no]!!.items.add(level)
                }
            }
        }

        return monkeys.values.sortedByDescending { it.times }.let { (first, second) ->
            first.times * second.times.toLong()
        }
    }

    fun part2(input: List<String>): Long {
        val monkeys = input.chunked(7).map { Monkey(it) }.associateBy { it.no }
        val mod = monkeys.values.map { it.divisible.toLong() }.reduce(Long::times)
        repeat(10000) {
            for (monkey in monkeys.values) {
                monkey.turn { it % mod }.map { (no, level) ->
                    monkeys[no]!!.items.add(level)
                }
            }
        }

        return monkeys.values.sortedByDescending { it.times }
            .take(2)
            .map { it.times.toLong() }
            .reduce(Long::times)
    }

    val testInput = readInput("Day11_test")
    print(part1(testInput))
    check(part1(testInput) == 10605L)
    print(part2(testInput))
    check(part2(testInput) == 2713310158)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}

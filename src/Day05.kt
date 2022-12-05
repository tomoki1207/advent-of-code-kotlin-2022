data class Operation(val move: Int, val from: Int, val to: Int)

fun main() {

    fun buildStacks(input: List<String>, stackCount: Int): List<ArrayDeque<Char>> {
        return input.map { line ->
            (1..stackCount).map { stackNo ->
                line.getOrElse(1 + (stackNo - 1) * 4) { ' ' }
            }
        }.let { list ->
            val stacks = (0 until stackCount).map { ArrayDeque<Char>() }
            // transpose
            list.reversed().forEach { crates ->
                for (i in (0 until stackCount)) {
                    val crate = crates[i]
                    if (!crate.isWhitespace()) {
                        stacks[i].add(crate)
                    }
                }
            }
            stacks
        }
    }

    fun buildOperations(input: List<String>): List<Operation> {
        val operation = "^move (\\d+) from (\\d+) to (\\d+)$".toRegex()

        return input.map {
            val groups = operation.find(it)!!.groupValues
            Operation(groups[1].toInt(), groups[2].toInt(), groups[3].toInt())
        }
    }

    fun part1(input: List<String>, stackHeight: Int, stackCount: Int): String {
        val stacks = buildStacks(input.take(stackHeight), stackCount)
        val operations = buildOperations(input.drop(stackHeight + 2))

        operations.forEach { operation ->
            val from = stacks[operation.from - 1]
            val to = stacks[operation.to - 1]
            repeat(operation.move) {
                from.removeLast().let { to.add(it) }
            }
        }

        return stacks.map { it.last() }.joinToString("")
    }

    fun part2(input: List<String>, stackHeight: Int, stackCount: Int): String {
        val stacks = buildStacks(input.take(stackHeight), stackCount)
        val operations = buildOperations(input.drop(stackHeight + 2))

        operations.forEach { operation ->
            val from = stacks[operation.from - 1]
            val to = stacks[operation.to - 1]
            (0 until operation.move)
                .map { from.removeLast() }
                .reversed()
                .let { to.addAll(it) }
        }

        return stacks.map { it.last() }.joinToString("")
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput, 3, 3) == "CMZ")
    check(part2(testInput, 3, 3) == "MCD")

    val input = readInput("Day05")
    println(part1(input, 8, 9))
    println(part2(input, 8, 9))
}

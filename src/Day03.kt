fun main() {
    val types = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

    fun part1(input: List<String>) = input.map { line ->
        val items = line.chunked(line.length / 2)
        items[0].toCharArray().intersect(items[1].toCharArray().toSet())
    }.sumOf { shared ->
        shared.sumOf { types.indexOf(it) + 1 }
    }


    fun part2(input: List<String>) = input.chunked(3)
        .map { lines ->
            lines.map { it.toCharArray() }.fold(types.toCharArray().toSet()) { a, b ->
                a.intersect(b.toSet())
            }
        }.sumOf { shared ->
            shared.sumOf { types.indexOf(it) + 1 }
        }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

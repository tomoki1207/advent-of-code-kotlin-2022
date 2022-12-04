fun main() {

    fun toPairs(input: List<String>) = input.map { line ->
        val pair = line.split(",")
        val first = pair[0].split("-").let { Pair(it[0].toInt(), it[1].toInt()) }
        val second = pair[1].split("-").let { Pair(it[0].toInt(), it[1].toInt()) }
        Pair((first.first..first.second).map { it }, (second.first..second.second).map { it })
    }

    fun part1(input: List<String>) = toPairs(input)
        .count { pair ->
            pair.first.containsAll(pair.second) || pair.second.containsAll(pair.first)
        }


    fun part2(input: List<String>) = toPairs(input)
        .count { pair ->
            pair.first.intersect(pair.second.toSet()).isNotEmpty()
        }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}

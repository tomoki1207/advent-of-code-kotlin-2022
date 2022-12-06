fun main() {

    fun findStartOfMarker(seq: String, windowSize: Int): Int {
        val found = seq.windowed(windowSize).first { it.toCharArray().toSet().size == windowSize }
        return seq.indexOf(found) + windowSize
    }

    fun part1(input: List<String>) = findStartOfMarker(input[0], 4)

    fun part2(input: List<String>) = findStartOfMarker(input[0], 14)

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 19)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}

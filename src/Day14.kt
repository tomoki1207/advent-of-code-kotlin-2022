import kotlin.math.max
import kotlin.math.min

data class Cave(val input: List<String>) {
    data class CavePoint(val x: Int, val y: Int)

    private val cave: MutableSet<CavePoint>
    private val maxDepth: Int

    init {
        val cave = input.flatMap { line ->
            val points = line.split("->").map { point ->
                point.trim().split(",").let { (x, y) -> x.toInt() to y.toInt() }
            }
            points.windowed(2) { (start, end) ->
                if (start.first != end.first) {
                    return@windowed (min(start.first, end.first)..max(start.first, end.first)).map { x ->
                        CavePoint(x, start.second)
                    }
                } else {
                    return@windowed (min(start.second, end.second)..max(start.second, end.second)).map { y ->
                        CavePoint(start.first, y)
                    }
                }
            }.flatten()
        }.toMutableSet()
        maxDepth = cave.maxOf { it.y }
        cave.addAll((-1000..1000).map { CavePoint(it, maxDepth + 2) })
        this.cave = cave
    }

    fun dropSand(x: Int = 500, y: Int = 0): Boolean {
        if (y > maxDepth) {
            return false
        }
        // bottom
        if (!cave.contains(CavePoint(x, y))) {
            return dropSand(x, y + 1)
        }
        // left
        if (!cave.contains(CavePoint(x - 1, y))) {
            return dropSand(x - 1, y)
        }
        // right
        if (!cave.contains(CavePoint(x + 1, y))) {
            return dropSand(x + 1, y)
        }
        cave.add(CavePoint(x, y - 1))
        return true
    }

    fun stackSand(x: Int = 500, y: Int = 0): Boolean {
        if (y == 0 && cave.contains(CavePoint(x, y))) {
            return false
        }
        // bottom
        if (!cave.contains(CavePoint(x, y))) {
            return stackSand(x, y + 1)
        }
        // left
        if (!cave.contains(CavePoint(x - 1, y))) {
            return stackSand(x - 1, y)
        }
        // right
        if (!cave.contains(CavePoint(x + 1, y))) {
            return stackSand(x + 1, y)
        }
        cave.add(CavePoint(x, y - 1))
        return true
    }
}

fun main() {

    fun solve(input: List<String>, process: (Cave) -> Boolean): Int {
        val cave = Cave(input)
        var step = 0
        while (process(cave)) {
            ++step
        }
        return step
    }

    fun part1(input: List<String>) = solve(input) { it.dropSand() }

    fun part2(input: List<String>) = solve(input) { it.stackSand() }

    val testInput = readInput("Day14_test")
    check(part1(testInput) == 24)
    check(part2(testInput) == 93)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}

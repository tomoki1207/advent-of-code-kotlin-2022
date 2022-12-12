import java.util.*

data class Point(val row: Int, val col: Int, val mark: Char) {
    val elevation = when (mark) {
        'S' -> 'a'
        'E' -> 'z'
        else -> mark
    }.code

    fun climbable(other: Point): Boolean {
        return elevation - other.elevation >= -1
    }
}

fun main() {

    fun solve(
        input: List<String>,
        determineStart: (Point) -> Boolean,
        determineEnd: (Point) -> Boolean,
        climbPredicate: (Point, Point) -> Boolean
    ): Int {
        var start: Point? = null
        val grid = input.mapIndexed { row, line ->
            row to line.mapIndexed { col, c ->
                val p = Point(row, col, c)
                if (determineStart(p)) {
                    start = p
                }
                col to p
            }.toMap()
        }.toMap()

        val closed = mutableSetOf<Point>()
        var open = LinkedList<Point>().also { it.add(start!!) }
        var step = 0
        while (true) {
            val tmp = LinkedList<Point>()
            while (open.isNotEmpty()) {
                val current = open.poll()
                if (determineEnd(current)) {
                    return step
                }
                if (!closed.add(current)) {
                    continue
                }

                // up,down,right,left
                grid[current.row - 1]?.get(current.col)?.let { up ->
                    if (climbPredicate(current, up)) {
                        tmp.add(up)
                    }
                }
                grid[current.row + 1]?.get(current.col)?.let { down ->
                    if (climbPredicate(current, down)) {
                        tmp.add(down)
                    }
                }
                grid[current.row]?.get(current.col - 1)?.let { left ->
                    if (climbPredicate(current, left)) {
                        tmp.add(left)
                    }
                }
                grid[current.row]?.get(current.col + 1)?.let { right ->
                    if (climbPredicate(current, right)) {
                        tmp.add(right)
                    }
                }
            }
            open = tmp
            step++
        }
    }

    fun part1(input: List<String>) = solve(input, { p -> p.mark == 'S' }, { p -> p.mark == 'E' },
        { curr, aimTo -> curr.climbable(aimTo) })

    fun part2(input: List<String>) = solve(input, { p -> p.mark == 'E' }, { p -> p.elevation == 'a'.code },
        { curr, aimTo -> aimTo.climbable(curr) })

    val testInput = readInput("Day12_test")
    check(part1(testInput) == 31)
    check(part2(testInput) == 29)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}

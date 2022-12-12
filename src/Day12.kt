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

        var open = LinkedList<Point>().also { it.add(start!!) }
        val closed = mutableSetOf<Point>()
        var step = 0
        while (true) {
            val tmp = LinkedList<Point>()
            while (open.isNotEmpty()) {
                val current = open.poll()
                if (determineEnd(current)) {
                    return step
                }
                if (closed.contains(current)) {
                    continue
                }
                closed.add(current)

                // up,down,right,left
                if (grid[current.row - 1]?.containsKey(current.col) == true) {
                    val up = grid[current.row - 1]!![current.col]!!
                    if (climbPredicate(current, up)) {
                        tmp.add(up)
                    }
                }
                if (grid[current.row + 1]?.containsKey(current.col) == true) {
                    val down = grid[current.row + 1]!![current.col]!!
                    if (climbPredicate(current, down)) {
                        tmp.add(down)
                    }
                }
                if (grid[current.row]?.containsKey(current.col - 1) == true) {
                    val left = grid[current.row]!![current.col - 1]!!
                    if (climbPredicate(current, left)) {
                        tmp.add(left)
                    }
                }
                if (grid[current.row]?.containsKey(current.col + 1) == true) {
                    val right = grid[current.row]!![current.col + 1]!!
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

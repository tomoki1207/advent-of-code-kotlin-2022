import kotlin.math.min

fun main() {

    fun withGrid(input: List<String>, operation: (Int, List<Int>, List<Int>, List<Int>, List<Int>) -> Unit) {
        val grid = input.map { line -> line.toList().map { it.digitToInt() } }
        val rowSize = grid.size
        val colSize = grid[0].size

        for (row in 0 until rowSize) {
            for (col in 0 until colSize) {
                val ups = if (row == 0) emptyList() else grid.take(row).map { it[col] }.reversed()
                val downs = if (row == rowSize - 1) emptyList() else grid.drop(row + 1).map { it[col] }
                val lefts = if (col == 0) emptyList() else grid[row].take(col).reversed()
                val rights = if (col == colSize - 1) emptyList() else grid[row].drop(col + 1)

                val tree = grid[row][col]
                operation(tree, ups, downs, lefts, rights)
            }
        }
    }

    fun part1(input: List<String>): Int {
        val visibleTrees = mutableListOf<Int>()
        withGrid(input) { tree, ups, downs, lefts, rights ->
            val visible = listOf(ups, downs, lefts, rights).any { direction ->
                direction.isEmpty() || direction.max() < tree
            }
            if (visible) {
                visibleTrees.add(tree)
            }
        }
        return visibleTrees.size
    }

    fun part2(input: List<String>): Int {
        val scores = mutableListOf<Int>()
        withGrid(input) { tree, ups, downs, lefts, rights ->
            listOf(ups, downs, lefts, rights)
                .map { direction ->
                    direction.takeWhile { it < tree }.count().let { min(it + 1, direction.size) }
                }
                .fold(1) { cur, value -> cur * value }
                .let { scores.add(it) }
        }

        return scores.max()
    }

    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}

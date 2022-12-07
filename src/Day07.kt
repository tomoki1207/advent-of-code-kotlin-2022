import Entry.Dir
import Entry.File

sealed class Entry(open val parent: Dir?, open val name: String) {
    abstract fun size(): Int
    data class File(override val parent: Dir, override val name: String, val size: Int) : Entry(parent, name) {
        override fun size() = size
    }

    data class Dir(override val parent: Dir?, override val name: String) : Entry(parent, name) {
        val entries: MutableSet<Entry> = mutableSetOf()

        override fun size() = entries.sumOf { it.size() }
    }
}

fun main() {

    fun execute(commands: List<String>, workingDir: Dir) {
        if (commands.isEmpty()) {
            return
        }

        val cmd = commands[0]

        // cd
        if (cmd.startsWith("$ cd")) {
            val rest = commands.drop(1)
            return when (val target = cmd.removePrefix("$ cd ")) {
                "/" -> execute(rest, workingDir) // for the first line
                ".." -> execute(rest, workingDir.parent!!)
                else -> execute(rest, workingDir.entries.filterIsInstance<Dir>().first { it.name == target })
            }
        }

        // ls
        if (cmd.startsWith("$ ls")) {
            val prints = commands.drop(1).takeWhile { !it.startsWith("$") }
            prints.forEach { print ->
                val entry = if (print.startsWith("dir")) {
                    Dir(workingDir, print.split(" ")[1])
                } else {
                    print.split(" ").let { File(workingDir, it[1], it[0].toInt()) }
                }
                workingDir.entries.add(entry)
            }
            return execute(commands.drop(1 + prints.size), workingDir)
        }

        // other command
        throw Error("Not Supported command. $cmd")
    }

    fun walk(dir: Dir): List<Entry> {
        val list = mutableListOf<Entry>(dir)

        val (dirs, files) = dir.entries.partition { it is Dir }
        dirs.flatMap { d -> walk(d as Dir) }.let { list.addAll(it) }
        list.addAll(files)
        return list
    }

    fun part1(input: List<String>): Int {
        val root = Dir(null, "/").also { execute(input, it) }
        return walk(root).filterIsInstance<Dir>()
            .filter { it.size() <= 100_000 }
            .sumOf { it.size() }
    }


    fun part2(input: List<String>): Int {
        val root = Dir(null, "/").also { execute(input, it) }
        val unused = 70_000_000 - root.size()

        return walk(root).filterIsInstance<Dir>()
            .filter { it.size() + unused > 30_000_000 }
            .minOf { it.size() }
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437)
    check(part2(testInput) == 24933642)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}

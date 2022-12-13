import DatumType.NumDatumType
import DatumType.PacketDatumType

sealed class DatumType {
    class NumDatumType(val value: Int) : DatumType() {
        override fun toString(): String {
            return value.toString()
        }
    }

    class PacketDatumType(val value: Packet) : DatumType() {
        override fun toString(): String {
            return "[$value]"
        }
    }
}

data class Packet(val data: List<DatumType>) : Comparable<Packet> {
    override fun toString(): String {
        return data.joinToString(",")
    }

    override fun compareTo(other: Packet): Int {
        if (this == other) {
            return 0
        }
        this.data.forEachIndexed { i, l ->
            val r = other.data.getOrNull(i) ?: return 1
            if (l is NumDatumType && r is NumDatumType) {
                if (l.value == r.value) {
                    return@forEachIndexed
                }
                return l.value - r.value
            }
            val lP = if (l is PacketDatumType) l.value else Packet(listOf(l as NumDatumType))
            val rP = if (r is PacketDatumType) r.value else Packet(listOf(r as NumDatumType))
            return lP.compareTo(rP)
        }
        return -1
    }
}

fun main() {

    fun parse(packet: String): Packet {
        var i = 0
        fun innerParse(): Packet {
            val packets = mutableListOf<DatumType>()
            while (i < packet.length && packet[i] != ']') {
                if (packet[i] == '[') {
                    ++i // [
                    packets.add(PacketDatumType(innerParse()))
                    ++i // ]
                } else {
                    // integer value
                    val s = i
                    while (packet[i] in '0'..'9') ++i
                    val numOrEmpty = packet.substring(s, i)
                    if (numOrEmpty.isNotEmpty()) {
                        packets.add(NumDatumType(numOrEmpty.toInt()))
                    }
                }
                if (packet.getOrNull(i) == ',') ++i // ,
            }
            return Packet(packets)
        }
        return innerParse()
    }

    fun part1(input: List<String>): Int {
        val packets = input.chunked(3) { (left, right) ->
            parse(left) to parse(right)
        }
        return packets.mapIndexed { i, (left, right) ->
            if (left <= right) {
                i + 1
            } else {
                null
            }
        }.filterNotNull().sum()
    }


    fun part2(input: List<String>): Int {
        val packets = input.toMutableList().also { it.addAll(listOf("[[2]]", "[[6]]")) }
            .filter { it.isNotEmpty() }
            .map { parse(it) }

        return packets.asSequence()
            .sorted()
            .map { it.toString() }
            .mapIndexed { i, p ->
                if (p == "[[2]]" || p == "[[6]]") i + 1 else null
            }
            .filterNotNull()
            .reduce(Int::times)
    }

    val testInput = readInput("Day13_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 140)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}

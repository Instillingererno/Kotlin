package algoritmerOgDatastrukturer

import java.io.File
import java.util.*
import java.util.stream.IntStream
import kotlin.collections.HashMap
import kotlin.streams.asSequence
import kotlin.streams.toList
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis


fun main(args: Array<String>) {


    val names = File("C:\\Users\\TDAT1337\\Documents\\GitHub\\Kotlin\\Oppgaver\\src\\algoritmerOgDatastrukturer\\Assets\\names.txt").readLines().mapIndexed { index, value -> value to index }

    val navnhasjTabell = HashTable<String, Int>(names.size)
            .apply {
                measureTimeMillis {
                    putAll(names)
                }.also { println("$it ms") }
            }

    println(navnhasjTabell)

    val average = navnhasjTabell.entries
            .mapNotNull { it?.run { it.children } }
            .sumByDouble { it.toDouble() } / names.size

    val loadFactor = navnhasjTabell.entries
            .count { it != null }.toDouble() / navnhasjTabell.entries.size


    println("Gjennomsnittlig kollisjon per index: $average")
    println("Loadfactor: $loadFactor")



    /*val source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

    val randomStrings = (0..5_000_000).map {
        Random().ints((5..10).random().toLong(),0, source.length)
                .asSequence()
                .map(source::get)
                .joinToString("")
    }.map { it to it } */

    val randomNrs = Random().ints().limit(5_000_000).mapToObj { it to it }.toList()


    println("\n\n\nStarting table init")

    val hasjTabell = HashTable<Int, Int>(randomNrs.size)
            .apply {
                measureTimeMillis {
                    putAll(randomNrs)
                }.also { println("$it ms") }
            }




    val javaTest = HashMap<Int, Int>(randomNrs.size)
            .apply {
                measureTimeMillis {
                    putAll(randomNrs)
                }.also { println("$it ms - std::java") }
            }

    print("\n\n")
    //println(hasjTabell.toString(100))








    val fil = File("index_med_barn.txt").writer()

    hasjTabell.entries.forEachIndexed { index, entry ->
        if(entry != null) fil.write("\n$index har ${entry.children} barn")
        //else fil.write("\n$index har ingen noder")
    }

    fil.close()


}


class HashTable<K, V>(private val initialSize: Int) {
    val MULTIPLIER = 31

    var counter = 0
    var timeToHash: Long = 0
    var accessTime: Long = 0

    val entries = Array<Entry<K, V>?>(initialSize) { null }

    fun putAll(collection: Collection<Pair<K, V>>) {
        collection.stream().parallel().forEach(::put)
        println()
    }


    fun put(pair: Pair<K, V>) {
        //measureNanoTime {
            index(pair.first).let {
                //print("\r${++counter}\t\t$timeToHash ns\t\t$accessTime ns")
                val entry = entries[it]
                if (entry == null) entries[it] = Entry(pair)
                else entry.put(Entry(pair))
                // for å dobbel hashe:
                // else put(it to pair.second)
            }
        //}.also { accessTime = it }
    }


    fun get(key: K) = entries[index(key)]?.get(key)



    private fun index(key: K): Int {
        var output: Int? = null
        //measureNanoTime {
            output = when (key) {
                is Int -> Math.abs(hash(key) % initialSize)
                is String -> Math.abs(hash(key) % initialSize)
                else -> Math.abs(key!!.hashCode() % initialSize)
            }
        //}.also { timeToHash = it }
        return output
    }

    fun hash(key: Int): Int {
        val h = (key * 0x9E3779B9).toInt() //Phi - gyldne snitt
        return h xor (h ushr 16)
    }

    fun hash(key: String): Int {
        var h = 0
        val chars = key.toCharArray()
        for (i in 0 until chars.size) {
            h = MULTIPLIER * h + chars[i].toInt()
        }
        return h xor (h ushr 16)
    }

    override fun toString() = entries.mapNotNull { it?.pprint() }.joinToString(separator = "")

    fun toString(limit: Int) = entries.sliceArray((0..limit)).mapNotNull { it?.pprint() }.joinToString(separator = "")



    class Entry<K, V>(val pair: Pair<K, V>, var next: Entry<K, V>? = null) {
        var children = 0

        override fun toString(): String = pprint()

        fun pprint(tab: Int = 0): String = StringBuilder().apply {
            val tabString = "\t".repeat(tab)
            append("$tabString(${pair.first}, ${pair.second})\n")
            if (next != null) append(next?.pprint(tab + 1))
        }.toString()

        fun put(entry: Entry<K, V>) {
            if (next == null) {
                children++
                next = entry
            } else {
                if (next!!.pair.first == entry.pair.first) next = entry
                else {
                    children++
                    next!!.put(entry)
                }
            }
        }

        fun get(key: K): V? {
            return if (pair.first == key) pair.second
                else {
                    if(next != null) next!!.get(key)
                    else null
                }
        }
    }
}
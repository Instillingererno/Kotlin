package algoritmerOgDatastrukturer

import java.lang.StringBuilder
import java.util.function.Consumer

/*
    Vi skal lage positive heltall som kan være vilkårlig store,
    ved å la hvert siffer være en node i ei lenket liste og hele lista
    være et tall

    a) Bruk dobbeltlenket liste og lag en metode for addisjon av to slike tall
    b) Lag en metode for subtraksjon
 */

fun main(args: Array<String>) {
    println(dobbelLenkeOf("12345678912345678912").add(dobbelLenkeOf("12345678912345678912")))
}


class DobbelLenke() {
    private var size = 0
    private var head: Node? = null
    private var tail: Node? = null



    fun size() = size

    fun addFirst(element: Byte) {
        val temp = Node(element, head, null)
        if (head != null) head!!.forrige = temp
        head = temp
        if (tail == null) tail = temp
        size++
    }

    fun addLast(element: Byte) {
        val temp = Node(element, null, tail)
        if (tail != null) tail!!.neste = temp
        tail = temp
        if (head == null) head = temp
        size++
    }

    fun descendingIterator(function: Consumer<Byte>) {
        var temp = tail
        while (temp != null) {
            function.accept(temp.siffer)
            temp = temp.forrige
        }
    }
    fun ascendingIterator(function: Consumer<Byte>) {
        var temp = head
        while (temp != null) {
            function.accept(temp.siffer)
            temp = temp.neste
        }
    }

    fun getFromLast(index: Int): Byte {
        if (index >= size) return 0
        var temp = tail
        for(i in 0 until index) temp = temp!!.forrige
        return temp!!.siffer
    }

    override fun toString(): String {
        return StringBuilder()
                .also { output -> ascendingIterator(Consumer { output.append(it) }) }
                .toString()
    }

    fun add(list: DobbelLenke): DobbelLenke {
        var carry = 0
        var nextNr: Byte = 0
        val largestSize = setOf(size, list.size()).max()!!
        val output = DobbelLenke()
        for (i in 0..largestSize) {
            if (i == largestSize && carry == 0) break
            nextNr = nextNr.plus(carry).plus(getFromLast(i)).plus(list.getFromLast(i)).toByte()
            carry = 0
            when (nextNr) {
                in 0..9 -> output.addFirst(nextNr)
                else -> {
                    carry++
                    output.addFirst(nextNr.minus(10).toByte())
                }
            }
            nextNr = 0
        }
        return output
    }

    fun subtract(list: DobbelLenke): DobbelLenke {
        return DobbelLenke()
    }

    class Node(var siffer: Byte, var neste: Node?, var forrige: Node?)
}

fun dobbelLenkeOf(vararg elements: Byte): DobbelLenke {
    return DobbelLenke().also { lenke -> elements.forEach { lenke.addLast(it) } }
}
fun dobbelLenkeOf(tall: String): DobbelLenke {
    return dobbelLenkeOf(*tall.toCharArray().filter { it.isDigit() }.map {Character.getNumericValue(it).toByte() }.toByteArray())
}
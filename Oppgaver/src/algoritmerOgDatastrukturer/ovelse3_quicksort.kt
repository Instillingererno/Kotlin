package algoritmerOgDatastrukturer

import java.lang.Exception
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.util.stream.IntStream
import java.util.stream.LongStream
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    /*
        Ta tiden på sortering av:
        - Tabell med helt tilfeldige data
        - Tabell med mange duplikater, f.eks hvor annenhvert element er likt.
        - Tabell som er sortert fra før, da dette er et vanlig spesialtilfelle

        Sjekk for korrekt sortering
     */

    /*
    Data:
        Test with 10000000 elements
            Tabell med tilfeldig data
                SinglePivot used: 965ms	Check: true
                DualPivot used: 994ms	Check: true
            Annenhvert element er 5000
                DualPivot used: 626ms	Check: true
                SinglePivot timed out after 30 minutes
            Tabell med sorterte verdier
                SinglePivot timed out after 30 minutes
                DualPivot timed out after 30 minutes
        Test with 100000000 elements
            Tabell med tilfeldig data
                SinglePivot used: 11796ms	Check: true
                DualPivot used: 12211ms	Check: true
            Annenhvert element er 5000
                DualPivot used: 8454ms	Check: true
                SinglePivot timed out after 30 minutes
            Tabell med sorterte verdier
                SinglePivot timed out after 30 minutes
                DualPivot timed out after 30 minutes
     */

    LongStream.of(10_000_000L, 100_000_000L).forEachOrdered { limit ->
        println("Test with $limit elements")
        for(i in 1..3) {
            val (text, array) = when (i) {
                1 -> "Tabell med tilfeldig data" to Random().ints().filter { it > 0 }.limit(limit).toArray()
                2 -> "Annenhvert element er 5000" to IntStream.iterate(1, Int::inc).limit(limit).map { if (it % 2 == 0) 5000 else Random().nextInt() }.toArray()
                3 -> "Tabell med sorterte verdier" to IntStream.iterate(1, Int::inc).limit(limit).map { it * 2 }.toArray()
                else -> throw Exception("Literally wtf")
            }
            println("\t$text")

            val singlePivot = CompletableFuture.supplyAsync {
                var singlePivot: SinglePivot? = null
                measureTimeMillis { singlePivot = SinglePivot(array.copyOf()) } to singlePivot!!.isGood
            }.completeOnTimeout(-1L to false, 30, TimeUnit.MINUTES).thenAccept {
                when (it.first) {
                    -1L -> println("\t\tSinglePivot timed out after 30 minutes")
                    else -> println("\t\tSinglePivot used: ${it.first}ms\tCheck: ${it.second}")
                }
            }

            val dualPivot = CompletableFuture.supplyAsync {
                var dualPivot: DualPivot? = null
                measureTimeMillis { dualPivot = DualPivot(array.copyOf()) } to dualPivot!!.isGood
            }.completeOnTimeout(-1L to false, 30, TimeUnit.MINUTES).thenAccept {
                when (it.first) {
                    -1L -> println("\t\tDualPivot timed out after 30 minutes")
                    else -> println("\t\tDualPivot used: ${it.first}ms\tCheck: ${it.second}")
                }
            }

            CompletableFuture.allOf(singlePivot, dualPivot).get()
        }
    }
}


class SinglePivot(val array: IntArray) {
    val isGood: Boolean by lazy { checkIfCorrect() }

    init {
        quicksort(0, array.size-1)
    }

    private fun quicksort(lo: Int, hi: Int) { if (lo < hi) {
        if (hi - lo < 30) insertsort(lo, hi+1)
        else {
            val middle = lo + (hi - lo)/2
            val pivot = array[middle]

            var (left, right) = lo to hi
            while (left <= right) {
                while (array[left] < pivot) {
                    left++
                }
                while (array[right] > pivot) {
                    right--
                }
                if (left <= right) {
                    array[left] = array[right].also { array[right] = array[left] }
                    left++
                    right--
                }
            }
            if (lo < hi) quicksort(lo, right)
            if (hi > left) quicksort(left, hi)
        }
    } }

    private fun insertsort(lo: Int, hi: Int) {
        for (i in lo+1 until hi) {
            for (j in i downTo 1) {
                if(array[j-1]>array[j]) {array[j-1]+=array[j]; array[j]=array[j-1]-array[j]; array[j-1]-=array[j]} else break
            }
        }
    }

    private fun checkIfCorrect(): Boolean {
        for (i in 0 until array.size-1) {
            if (array[i] > array[i+1]) return false
        }
        return true
    }
}

class DualPivot(val array: IntArray) {
    val isGood: Boolean by lazy { checkIfCorrect() }

    init {
        quicksort(0, array.size-1)
    }

    fun quicksort(lo: Int, hi: Int) { if (lo < hi) {
        if (hi - lo < 30) insertsort(lo, hi+1)
        else {
            array[lo] = array[(hi - lo) / 3 + lo].also { array[(hi - lo) / 3 + lo] = array[lo] }
            array[hi] = array[(hi - lo) * 2 / 3 + lo].also { array[(hi - lo) * 2 / 3 + lo] = array[hi] }
            if(array[hi] < array[lo]) array[hi] = array[lo].also { array[lo] = array[hi] }
            var (left, right) = lo + 1 to hi - 1
            var current = lo + 1
            while (current <= right) {
                if (array[current] < array[lo]) {
                    array[left] = array[current].also { array[current] = array[left] }
                    left++
                    current++
                } else if (array[hi] < array[current]) {
                    array[current] = array[right].also { array[right] = array[current] }
                    right--
                } else {
                    current++
                }
            }
            left--
            right++
            array[lo] = array[left].also { array[left] = array[lo] }
            array[hi] = array[right].also { array[right] = array[hi] }

            quicksort(lo, left -1)
            if(array[left] < array[right]) quicksort(left + 1, right -1)
            quicksort(right + 1, hi)
        }
    } }

    fun insertsort(lo: Int, hi: Int) {
        for (i in lo+1 until hi) {
            for (j in i downTo 1) {
                if(array[j-1]>array[j]) {array[j-1]+=array[j]; array[j]=array[j-1]-array[j]; array[j-1]-=array[j]} else break
            }
        }
    }

    private fun checkIfCorrect(): Boolean {
        for (i in 0 until array.size-1) {
            if (array[i] > array[i+1]) return false
        }
        return true
    }
}




package algoritmerOgDatastrukturer

import java.util.*
import java.util.stream.IntStream
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    val limit = 100_000_000L
    val testArray = Random().ints().filter { it > 0 }.limit(limit).toArray() //intArrayOf(7,45,2,3,65,78,2,12,4,7,4,45,8,5,34,23,4,67,78,654,4,5456,453,2,5,34,3,34,6,6,213)

    var singlePivotTest: SinglePivot? = null
    var dualPivotTest: DualPivot? = null

    measureTimeMillis {
        singlePivotTest = SinglePivot(testArray.copyOf())
    }.also { println("Single pivot with $limit ints: $it ms") }

    measureTimeMillis {
        dualPivotTest = DualPivot(testArray.copyOf())
    }.also { println("Dual pivot with $limit ints: $it ms") }

    val checkArray = dualPivotTest!!.array.sliceArray(0..100)
    var check = true
    for(i in 0 until checkArray.size-1) {
        if (checkArray[i] > checkArray[i+1]) {
            check = false
            println("${checkArray[i]} > ${checkArray[i+1]}")
        }
    }
    println(check)
    println(Arrays.toString(checkArray))
}


class SinglePivot(val array: IntArray) {
    init {
        quicksort(0, array.size-1)
    }

    fun quicksort(lo: Int, hi: Int) { if (lo < hi) {
        if (hi - lo < 80) insertsort(lo, hi+1)
        else {
            val p = partition(lo, hi)
            //launch { quicksort(lo, p-1) }
            quicksort(lo, p-1)
            //launch { quicksort(p+1, hi) }
            quicksort(p+1, hi)
        }
    } }

    fun partition(lo: Int, hi: Int): Int {
        val pivot = array[hi]
        var i = lo
        for (j in lo until hi) {
            if (array[j] < pivot) {
                array[i] = array[j].also { array[j] = array[i] }
                i++
            }
        }
        array[i] = array[hi].also { array[hi] = array[i] }
        return i
    }

    fun insertsort(lo: Int, hi: Int) {
        for (i in lo+1 until hi) {
            for (j in i downTo 1) {
                if(array[j-1]>array[j]) {array[j-1]+=array[j]; array[j]=array[j-1]-array[j]; array[j-1]-=array[j]} else break
            }
        }
    }
}

class DualPivot(val array: IntArray) {
    init {
        quicksort(0, array.size-1)
    }

    fun quicksort(lo: Int, hi: Int) { if (lo < hi) {
        if (hi - lo < 80) insertsort(lo, hi+1)
        else {
            //lp = left pivot, rp = right pivot
            val (lp, rp) = partition(lo, hi)
            quicksort(lo, lp-1)
            quicksort(lp+1, rp-1)
            quicksort(rp+1, hi)
        }
    } }

    fun partition(lo: Int, hi: Int): Pair<Int, Int> {
        if(array[lo] > array[hi])
            array[lo] = array[hi].also { array[hi] = array[lo] }
        var j = lo + 1
        var g = hi - 1
        var k = lo + 1
        val p = array[lo]
        val q = array[hi]
        while (k <= g) {
            if (array[k] < p) {
                array[k] = array[j].also { array[j] = array[k] }
                j++
            }
            // if elements are greater than or equal
            // to the right pivot
            else if (array[k] >= q) {
                while (array[g] > q && k < g)
                    g--
                array[k] = array[g].also { array[g] = array[k] }
                g--
                if (array[k] < p) {
                    array[k] = array[j].also { array[j] = array[k] }
                    j++
                }
            }
            k++
        }
        j--
        g++

        // Bring pivots to their appropriate positions
        array[lo] = array[j].also { array[j] = array[lo] }
        array[hi] = array[g].also { array[g] = array[hi] }

        return j to g
    }

    fun insertsort(lo: Int, hi: Int) {
        for (i in lo+1 until hi) {
            for (j in i downTo 1) {
                if(array[j-1]>array[j]) {array[j-1]+=array[j]; array[j]=array[j-1]-array[j]; array[j-1]-=array[j]} else break
            }
        }
    }
}




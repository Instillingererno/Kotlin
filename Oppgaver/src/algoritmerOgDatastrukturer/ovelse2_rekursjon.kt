package algoritmerOgDatastrukturer

import java.util.stream.IntStream
import kotlin.system.measureTimeMillis


fun main (args: Array<String>) {

    val maxPowers = 1_000_000_000L

    val recursivePowerTime = measureTimeMillis {
        val sum = IntStream.iterate(1, Int::inc).limit(maxPowers).mapToDouble { recursivePower(it, 10) }.sum()
        println(sum)
    }

    val recursivePower2Time = measureTimeMillis {
        val sum = IntStream.iterate(1, Int::inc).limit(maxPowers).mapToDouble { recursivePower2(it, 10) }.sum()
        println(sum)
    }

    val javaPowerTime = measureTimeMillis {
        val sum = IntStream.iterate(1, Int::inc).limit(maxPowers).mapToDouble { Math.pow(it.toDouble(), 10.0) }.sum()
        println(sum)
    }

    println("""

        2.1-1
        1^10 -> $maxPowers^10
        tid: $recursivePowerTime ms

        2.2-1
        1^10 -> $maxPowers^10
        tid: $recursivePower2Time ms

        Java: Math.pow tid: $javaPowerTime ms
    """.trimIndent())
}

fun recursivePower (number: Number, pow: Int) : Double { // 2.1-1
    return when (pow) {
        0 -> 1.0
        else -> number.toDouble() * recursivePower(number, pow - 1)
    }
}

fun recursivePower2 (number: Number, pow: Int) : Double { //2.2-1
    val numberDouble = number.toDouble()
    return when {
        pow == 0 -> 1.0
        pow % 2 != 0 -> numberDouble * recursivePower2(numberDouble * numberDouble, (pow-1) / 2)
        else -> recursivePower2(numberDouble * numberDouble, pow / 2)
    }
}
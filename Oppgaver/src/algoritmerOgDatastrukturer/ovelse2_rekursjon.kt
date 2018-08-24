package algoritmerOgDatastrukturer



fun main (args: Array<String>) {
    println("""
        2.1-1
        5^2 = ${recursivePower(5, 2)}
        10^1 = ${recursivePower(10, 1)}
        25^10 = ${recursivePower(25, 10)}

        2.2-1
        5^2 = ${recursivePower2(5, 2)}
        10^1 = ${recursivePower2(10, 1)}
        25^10 = ${recursivePower2(25, 10)}
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
        pow % 2 == 0 -> numberDouble * recursivePower2(numberDouble * numberDouble, pow / 2)
        else -> recursivePower2(numberDouble * numberDouble, (pow - 1) / 2)
    }
}
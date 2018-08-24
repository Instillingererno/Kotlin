import java.math.BigInteger

fun main(args: Array<String>) {

    val startTime = System.currentTimeMillis()
    val test = BigInteger("987654321").pow(300_000)
    val endTime = System.currentTimeMillis()

    println("${test.bitLength()} Time: ${endTime - startTime}ms")
}
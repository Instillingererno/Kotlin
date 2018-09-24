import java.math.BigInteger
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {

    println(BigInteger("12345678901234567890123456789012345678901234567890123456789012345678901234567890").pow(2)).also {
        println(it.)
    }

    /*
    measureTimeMillis {
        BigInteger("75").modPow(BigInteger("98320831902138712308"), BigInteger("37")).also {
            println(it)
        }.also { println("$it ms") }
    }
    */
}
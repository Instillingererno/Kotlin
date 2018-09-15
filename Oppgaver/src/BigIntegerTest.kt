import java.math.BigInteger
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {

    measureTimeMillis {
        BigInteger("75").modPow(BigInteger("98320831902138712308"), BigInteger("37")).also {
            println(it)
        }.also { println("$it ms") }
    }
}
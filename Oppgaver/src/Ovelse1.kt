import java.util.stream.IntStream
import kotlin.math.pow

fun main(args: Array<String>) {
    
}


fun toSeconds(hours: Int = 0, minutes: Int = 0, seconds: Int = 0) = hours * 360 + minutes * 60 + seconds

fun toHoursMinutesSeconds(seconds: Int): String {
    val input = ReducingInt(seconds)
    return listOf(2,1,0).map { input.divide(60.0.pow(it).toInt())}.joinToString(separator = " : ")
}

class ReducingInt(var number: Int = 0) {
    fun divide(with: Int): Int {
        println("$number with $with")
        val max = IntStream.iterate(1) { it + 1 }.takeWhile { number / (with * it) >= 1 }.max().orElse(0)
        number -= with * max
        return max
    }
}
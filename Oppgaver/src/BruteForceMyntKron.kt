import java.util.stream.IntStream
import java.util.stream.LongStream

fun main(args: Array<String>) {
    //val bits: Long = 3
    val sequence = 7

    LongStream.rangeClosed(7,21).forEach { bits ->
        val myntKronSequence = MyntKronSequence(bits, sequence)

        println("""
            sequence: ${ myntKronSequence.sequenceAcurance }
    """.trimIndent())
    }
}

class MyntKronSequence(val nrBits: Long, val sequence: Int) {
    val totalTries: Long = Math.pow(2.0, nrBits.toDouble()).toLong()
    val sequenceAcurance: Long

    init {
        println("Starting")
        val startTime = System.currentTimeMillis()
        sequenceAcurance = LongStream.range(0, totalTries)
                .parallel()
                .mapToObj { checkSequence(it, sequence) }
                .filter { it }
                .count()
        println("Operation took ${System.currentTimeMillis() - startTime}ms")
    }

    private fun checkSequence(number: Long, nrBits: Int): Boolean {
        var mutNumber = number
        var sequenceCounter = 0
        var duplicates = 0

        while(mutNumber != 0L) {
            if (mutNumber % 2L != 0L) {
                sequenceCounter++
                if(sequenceCounter == nrBits) duplicates++
                if(duplicates > 1) return true
            } else {
                sequenceCounter = 0
            }
            mutNumber = mutNumber ushr 1
        }
        return false
    }
}
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.util.concurrent.atomic.AtomicInteger
import java.util.stream.IntStream
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    println("For loop: ${measureTimeMillis { forLoop() }}ms")
    //println("StandardThreads: ${measureTimeMillis { standardThreads() }}ms")
    println("StreamThreads: ${measureTimeMillis { streamThreads() }}ms")
    println("CoRoutines: ${measureTimeMillis { coRoutines() }}ms")
}

fun forLoop() {
    val c = AtomicInteger()
    for (i in 1..1_000_000) c.addAndGet(i)
    println(c.get())
}

fun standardThreads() {
    val c = AtomicInteger()
    for (i in 1..1_000_000) thread(start = true) { c.addAndGet(i) }
    println(c.get())
}
fun streamThreads() {
    val c = AtomicInteger()
    IntStream.rangeClosed(1, 1_000_000).forEach { c.addAndGet(it) }
    println(c.get())
}
fun coRoutines() {
    val c = AtomicInteger()
    (1..1_000_000).forEach { c.addAndGet(it) }
    println(c.get())
}
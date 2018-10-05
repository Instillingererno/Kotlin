package algoritmerOgDatastrukturer

import java.io.File
import java.util.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.Executor
import java.util.concurrent.ForkJoinPool
import java.util.stream.IntStream
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {

    val file = File("C:\\Users\\KALAM\\Documents\\Kotlin\\Oppgaver\\src\\algoritmerOgDatastrukturer\\Assets\\L7g6.txt").bufferedReader()

    val regex = Regex("(\\D+)")

    val size = file.readLine().split(regex)[0].toInt()

    val graph = Graph(size).also { graph ->
        measureTimeMillis {
            file.lines()
                    .parallel()
                    .forEach {
                        it.split(regex).also { stringArray ->
                            graph.addConnection(stringArray[0].toInt(), stringArray[1].toInt())
                        }
                    }
        }.also { println("File init took $it ms") }
    }
}


class Graph(initialSize: Int) {
    /*
    node_index    ref_child   lower   upper   ref_temp
    |           |           |       |       |
    |           |           |       |       |
    |           |           |       |       |
     */

    val nodes = Array(initialSize) { index -> Node(index) }

    /*
    TODO: Implement
       DFS
       Transpose
       Sort
     */

    fun runPipeline() {
        measureTimeMillis {
            applyDFS()
        }.also { println("1st DFS took $it ms") }

        measureTimeMillis {
            sort()
        }.also { println("Sort took $it ms") }

        measureTimeMillis {
            transpose()
        }.also { println("Transposing took $it ms") }

        measureTimeMillis {
            applyDFS()
        }.also { println("2nd DFS took $it ms") }
    }

    fun applyDFS() {
        fun findNextUnFound() = nodes.firstOrNull { !it.found }

        nodes.forEach { it.lower = -1; it.upper = -1 }

        var count = 0
        val threadPool = ForkJoinPool()
        val linkedLists = mutableListOf<LinkedList<Int>>()
        val stack = Stack<Int>()

        for (node in nodes) {
            if (node.found) continue
            val linkedList = LinkedList<Int>()
            var n: Node? = node

            while (stack.count() > 0 || n != null) {


            }
        }

    }

    fun sort() {
        nodes.sortByDescending { it.upper }
    }

    fun transpose() {
        Arrays.stream(nodes)
                .parallel()
                .forEach { node ->
                    node.neighbours.forEach { neighbour ->
                        nodes[neighbour].temp.add(node.id)
                    }
                }
        nodes.forEach { node -> node.neighbours = node.temp }
    }

    fun addConnection(fromNode: Int, toNode: Int) = nodes[fromNode].neighbours.add(toNode)


    class Node(
            val id: Int,
            var neighbours: MutableList<Int> = mutableListOf(),
            var found: Boolean = false,
            var lower: Int = -1,
            var upper: Int = -1,
            var temp: MutableList<Int> = mutableListOf()
    )
}

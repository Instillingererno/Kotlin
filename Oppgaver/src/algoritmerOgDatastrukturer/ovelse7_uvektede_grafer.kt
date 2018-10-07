package algoritmerOgDatastrukturer

import java.io.File
import java.util.*
import java.util.concurrent.*
import java.util.stream.IntStream
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {

    val file = File("C:\\Users\\TDAT1337\\Documents\\GitHub\\Kotlin\\Oppgaver\\src\\algoritmerOgDatastrukturer\\Assets\\L7Skandinavia.txt").bufferedReader()

    val regex = Regex("(\\D+)")

    val size = file.readLine().split(regex)[0].toInt()


    val graph = Graph(size).also { graph ->
        measureTimeMillis {
            file.lines()
                    .parallel()
                    .forEach {
                        it.split(regex).filter { stringArray -> stringArray != "" }.also { stringArray ->
                            graph.addConnection(stringArray[0].toInt(), stringArray[1].toInt())
                        }
                    }
        }.also { println("File init took $it ms") }
    }

    graph.runPipeline()
}


class Graph(initialSize: Int) {
    /*
    node_index    ref_child   lower   upper   ref_temp
    |           |           |       |       |
    |           |           |       |       |
    |           |           |       |       |
     */

    val nodes = Array(initialSize) { index -> Node(index) }

    var threadPool: ExecutorService? = null
    val linkedLists = mutableListOf<LinkedList<Node>>()

    private var count = 32
    private val countDelta = -10

    private var finished = false


    /*
    TODO: Implement
       DFS
       Transpose
       Sort
     */

    fun runPipeline() {
        measureTimeMillis {

            measureTimeMillis {
                applyDFS()
            }.also { println("1st DFS took $it ms") }


            //nodes.forEach { println("${it.id}: ${it.lower}") }

            measureTimeMillis {
                transpose()
            }.also { println("Transposing took $it ms") }


            measureTimeMillis {
                sort()
            }.also { println("Sort took $it ms") }



            measureTimeMillis {
                applyDFS()
            }.also { println("2nd DFS took $it ms") }


        }.also { println("Total time was $it ms") }

        println("Graph had ${linkedLists.size} strong connections")

        //nodes.forEach { println("${it.id}: ${it.neighbours.size}") }
    }


    private fun assignWork(linkedList: LinkedList<Node>, root: Node, count: Int) {

        threadPool!!.submit {
            var node: Node? = root
            var localCount = count -1
            while (node != null) {
                localCount-= 1
                if (!node.found) {
                    //linkedList.add(node.apply { lower = localCount; found = true })
                    node.apply { lower = localCount; found = true }
                    when(node.neighbours.size) {
                        0 -> {
                            node = null
                        }
                        1 -> node = node.neighbours[0]
                        else -> node.neighbours.apply {
                            (1 until this.size).forEach { assignWork(
                                    linkedList,
                                    node!!.neighbours[it],
                                    localCount + countDelta
                            ) }
                            node = node!!.neighbours[0]
                        }
                    }
                } else {
                    break
                }
            }
        }

    }

    private fun invokeNewLinkedList(node: Node) {
        count+= countDelta
        linkedLists.add(LinkedList<Node>().also { linkedList ->
            assignWork(linkedList, node, count)
        })
    }

    fun applyDFS() {
        nodes.forEach {
            it.apply {
                lower = -1
                upper = -1
                found = false
            }
        }
        finished = false
        linkedLists.clear()
        for (node in nodes) {
            if(!node.found) {
                threadPool = Executors.newWorkStealingPool()
                invokeNewLinkedList(node)
                threadPool!!.shutdown()
                threadPool!!.awaitTermination(1, TimeUnit.SECONDS)
            }
        }
    }

    fun sort() {
        //nodes.groupingBy { it.lower }.eachCount().filter { it.value > 1 }.also { println(it) }
        nodes.sortByDescending { it.lower }
    }

    fun transpose() {
        Arrays.stream(nodes)
                .parallel()
                .forEach {
                    it.neighbours.forEach { neighbour ->
                        neighbour.temp.add(it)
                    }
                }
        nodes.forEach { node -> node.neighbours = node.temp }

    }

    fun addConnection(fromNode: Int, toNode: Int) = nodes[fromNode].neighbours.add(nodes[toNode])


    class Node(
            val id: Int,
            var neighbours: MutableList<Node> = mutableListOf(),
            var found: Boolean = false,
            var lower: Int = -1,
            var upper: Int = -1,
            var temp: MutableList<Node> = mutableListOf()
    )
}

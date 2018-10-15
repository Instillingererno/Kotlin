package algoritmerOgDatastrukturer

import java.io.File
import java.util.*
import java.util.function.Consumer
import kotlin.system.measureTimeMillis


fun main(args: Array<String>) {
    val fil = File("C:\\Users\\TDAT1337\\Documents\\GitHub\\Kotlin\\Oppgaver\\src\\algoritmerOgDatastrukturer\\Assets\\vgSkandinavia.txt").bufferedReader()
    val regex = Regex("(\\D+)")
    val size = fil.readLine().split(regex)[0].toInt()

    val graph = WeightedGraph(size).also { graph ->
        measureTimeMillis {
            fil.lines()
                    .parallel()
                    .forEach {
                        it.split(regex).filter(String::isNotEmpty).map(String::toInt).also { arr ->
                            graph addEdge Triple(arr[0], arr[1], arr[2])
                        }
                    }
        }.also { println("File init took $it ms") }
    }.also {
        if(it.size() < 50) it prettyPrint 50
    }

    runPipeline(graph)
}


fun runPipeline(graph: WeightedGraph) {

    measureTimeMillis {
        graph.runDijkstra(graph[1])
    }.also { println("Dijkstra took $it ms") }

}




class WeightedGraph(initialSize: Int) {





    fun runDijkstra(start: Node) {
        val queue = PriorityQueue<Node>(Comparator.comparingInt(Node::priority))
        queue.add(start.apply { priority = 0 })
        while (queue.isNotEmpty()) {
            val node = queue.poll()
            //println("Hi, ${node.id}")
            node.forEachEdge {
                if (it.to.priority > node.priority + it.weight) {
                    it.to.apply {
                        prevNode = node
                        priority = node.priority + it.weight
                    }
                    //if (!queue.contains(it.to))
                        queue.add(it.to)
                }
            }
        }

        if(size() < 50) {
            nodes.forEach {
                println("${it.id}   ${it.prevNode?.id}  ${it.priority}")
            }
        }
    }






    private val nodes = Array(initialSize) { Node(it) }

    operator fun get(index: Int) = nodes[index]

    infix fun addEdge(triple: Triple<Int, Int, Int>) = run { nodes[triple.first] += Edge(nodes[triple.second], triple.third) }

    infix fun prettyPrint(nrLines: Int) = nodes.take(nrLines).forEach(::println)

    fun size() = nodes.size

    class Node(
            val id: Int,
            val edges: MutableList<Edge> = mutableListOf(),
            var priority: Int = Int.MAX_VALUE, // equates to distance
            var prevNode: Node? = null) {
        operator fun get(index: Int) = edges[index]
        fun forEachEdge(action: (Edge) -> Unit) = edges.forEach(action)
        override fun toString() = "$id -> [${edges.joinToString(", ") { "${it.to.id} |${it.weight}|" }}]"
        operator fun plusAssign(edge: Edge) = edges.add(edge).let { Unit }
    }
    class Edge(val to: Node, val weight: Int)

}
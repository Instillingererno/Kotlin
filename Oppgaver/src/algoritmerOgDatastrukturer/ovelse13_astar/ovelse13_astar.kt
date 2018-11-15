package algoritmerOgDatastrukturer.ovelse13_astar

import java.io.File
import java.util.*
import kotlin.system.measureTimeMillis



fun Double.toRad() = this * Math.PI / 180
fun Double.toDeg() = this * 180 / Math.PI

fun Double.square() = this * this


fun main(args: Array<String>) {

    val nodeFile = File("C:\\Users\\TDAT1337\\Documents\\GitHub\\Kotlin\\Oppgaver\\src\\algoritmerOgDatastrukturer\\ovelse13_astar\\files\\noder.txt")
    val edgeFile = File("C:\\Users\\TDAT1337\\Documents\\GitHub\\Kotlin\\Oppgaver\\src\\algoritmerOgDatastrukturer\\ovelse13_astar\\files\\kanter.txt")

    val graph = WeightedGraph(nodeFile, edgeFile)

    measureTimeMillis {
        graph.dijkstra(2806952, 4402079)
    }.also { println("Dijkstra took $it ms") }
    graph.write(2806952, 4402079, File("C:\\Users\\TDAT1337\\Documents\\GitHub\\Kotlin\\Oppgaver\\src\\algoritmerOgDatastrukturer\\ovelse13_astar\\files\\dijkstra.txt"))
    println("finished writing file")


    graph.resetCosts()

    measureTimeMillis {
        graph.astar(2806952, 4402079)
    }.also { println("Astar took $it ms") }
    graph.write(2806952, 4402079, File("C:\\Users\\TDAT1337\\Documents\\GitHub\\Kotlin\\Oppgaver\\src\\algoritmerOgDatastrukturer\\ovelse13_astar\\files\\astar.txt"))
    println("finished writing file")

}





class WeightedGraph(nodes: File, edges: File) {
    val nodes: Array<Node> = initArrayFromFile(nodes, edges)


    private fun initArrayFromFile(nodes: File, edges: File): Array<Node> {
        val regex = Regex("[^\\d.]+")
        val nodesReader = nodes.bufferedReader()
        val edgeReader = edges.bufferedReader()
        val initialSize = nodesReader.readLine().trim().toInt()
        return Array(initialSize) { Node(it) }.apply {
            measureTimeMillis {
                nodesReader.lines().parallel()
                        .forEach { string ->
                            string.split(regex).also {
                                this[it[0].toInt()].apply {
                                    setCord (it[1].toDouble() to it[2].toDouble())
                                    breddeCos = Math.cos(bredde.toRad())
                                }
                            }
                        }
            }.also { println("Reading nodes file took $it ms") }

            measureTimeMillis {
                edgeReader.lines().skip(1).parallel()
                        .forEach { string ->
                            string.split(regex).also {
                                this[it[0].toInt()] += Edge(this[it[1].toInt()], it[2].toInt())
                            }
                        }
            }.also { println("Reading edges file took $it ms") }
        }
    }


    fun write(from: Int, to: Int, file: File) {
        val startNode = nodes[from]
        val endNode = nodes[to]
        val output = mutableListOf(endNode)
        var current = endNode.prevNode
        while (current != startNode) {
            output.add(current!!)
            current = current.prevNode
        }
        output.add(startNode)
        val temp = output.asReversed().map { "${it.bredde}, ${it.lengde}" }
        file.writeText(temp.joinToString("\n"))
    }



    fun dijkstra(start: Int, end: Int) {
        val startNode = nodes[start]
        val endNode = nodes[end]
        val queue = PriorityQueue<Node>(Comparator.comparingInt(Node::gCost))
        queue.add(startNode.apply { gCost = 0; found = true })
        while (queue.isNotEmpty()) {
            val node = queue.poll()
            //print("\r${node.id}")
            if(node == endNode) break
            node.edges.forEach {
                if (it.to.gCost > node.gCost + it.time) {
                    it.to.apply {
                        prevNode = node
                        gCost = node.gCost + it.time
                        found = false
                    }
                }
                if (!it.to.found) queue.add(it.to.apply { found = true })
            }
        }
    }


    fun astar(start: Int, end: Int) {
        // Best first heuristic search  f(n) = g(n) + h(n), der g(n) -> exakt kost og  h(n) -> estimert kost
        val startNode = nodes[start]
        val endNode = nodes[end]
        val queue = PriorityQueue<Node>(Comparator.comparingInt(Node::hCost))
        queue.add(startNode.apply { gCost = 0; hCost = (this estimateDistanceTo endNode); found = true })
        while (queue.isNotEmpty()) {
            val node = queue.poll()
            //print("\r${node.id}")
            //print("\r$queue")
            if (node == endNode) break
            node.edges.forEach {
                if (it.to.gCost > node.gCost + it.time) {
                    it.to.apply {
                        prevNode = node
                        gCost = node.gCost + it.time
                        hCost = gCost + (this estimateDistanceTo endNode)
                        found = false
                    }
                }
                if (!it.to.found) queue.add(it.to.apply { found = true })
            }
        }
    }


    fun resetCosts() = nodes.forEach { it.apply {
                gCost = Int.MAX_VALUE
                hCost = Int.MAX_VALUE
                found = false } }


    class Node(val id: Int) {
        val edges: MutableList<Edge> = mutableListOf()
        var gCost: Int = Int.MAX_VALUE
        var hCost: Int = Int.MAX_VALUE
        var prevNode: Node? = null
        var bredde: Double = 0.0
        var lengde: Double = 0.0
        var breddeCos: Double = 0.0
        var found = false

        operator fun get(index: Int) = edges[index]
        override fun toString() =
                "$id -> prevNode: $prevNode, gCost: $gCost"

        operator fun plusAssign(edge: Edge) { edges.add(edge) }
        infix fun setCord(pair: Pair<Double, Double>) { bredde = pair.first; lengde = pair.second }

        infix fun sinBredde(n2: Node) =
                Math.sin((this.bredde.toRad() - n2.bredde.toRad()) / 2.0)

        infix fun sinLengde(n2: Node) =
                Math.sin((this.lengde.toRad() - n2.lengde.toRad()) / 2.0)

        // From 2 * r / s * t * 100, der r er jordens radius, s er hastighet og t er tid faktor
        val constant = 41701090.90909090909090909091
        infix fun estimateDistanceTo(to: Node) =
                (constant * Math.asin(
                        Math.sqrt(
                                (this sinBredde to).square() + this.breddeCos * to.breddeCos * (to sinLengde to).square()
                        )
                )).toInt()

    }

    class Edge(val to: Node, val time: Int)
}
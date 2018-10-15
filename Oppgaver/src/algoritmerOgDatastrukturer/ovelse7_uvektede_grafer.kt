package algoritmerOgDatastrukturer

import java.io.File
import java.util.*
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

    var linkedLists = mutableListOf<LinkedList<Node>>()


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



            measureTimeMillis {
                transpose()
            }.also { println("Transposing took $it ms") }


            measureTimeMillis {
                sort()
            }.also { println("Sort took $it ms") }

            //nodes.forEach { println("${it.id}: ${it.lower}") }

            //println()
            /*
            nodes.forEach {
                println("${it.id}: ${it.neighbours.map(Node::id)}")
            }
            */

            measureTimeMillis {
                applyDFS()
            }.also { println("2nd DFS took $it ms") }


        }.also { println("Total time was $it ms") }

        println()

        println("Graph had ${linkedLists.size} strong connections")

        println()

        /*
        linkedLists.forEach {
            println(it.map(Node::id))
        }
        */

        //nodes.forEach { println("${it.id}: ${it.neighbours.size}") }
    }


    fun applyDFS() {

        var count = Long.MIN_VALUE
        val countDelta = 2000L

        nodes.forEach {
            it.apply {
                found = false
            }
        }
        linkedLists.clear()

        var strongConnection = 0
        val stack = Stack<Node>()
        val linkedLists: MutableList<LinkedList<Node>> = mutableListOf()

        for (node in nodes) {
            if (!node.found) {
                strongConnection++
                count += countDelta * strongConnection
                val linkedList = LinkedList<Node>()
                linkedLists.add(linkedList)
                node.lower = count
                stack.push(node)

                tailrec fun followLeftTrail(node: Node, linkedList: LinkedList<Node>) {
                    if (node.found) return
                    count--
                    linkedList.add(node)
                    node.apply {
                        found = true
                        lower = count
                    }
                    when (node.neighbours.size) {
                        0 -> return
                        1 -> followLeftTrail(node.neighbours[0], linkedList)
                        else -> {
                            for (i in node.neighbours.size-1 downTo 1) stack.push(node.neighbours[i])
                            followLeftTrail(node.neighbours[0], linkedList)
                        }
                    }
                }

                while(!stack.empty()) {
                    followLeftTrail(stack.pop(), linkedList)
                }
            }

            this.linkedLists = linkedLists
        }


        /*
        linkedLists.forEach {
            println("${it.map(Node::id)}")
        }
        */
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
            var lower: Long = 0L,
            var nrFoundChildren: Int = 0,
            var temp: MutableList<Node> = mutableListOf()
    )
}

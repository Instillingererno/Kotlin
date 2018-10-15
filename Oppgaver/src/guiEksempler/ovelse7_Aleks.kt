package guiEksempler


import java.io.File
import java.util.*
import kotlin.system.measureTimeMillis

class Node (var id: Int, var found: Int = 0, var done: Int = 0, var nodes: ArrayList<Node> = ArrayList(), var temp: ArrayList<Node> = ArrayList()){
    fun connect(node: Node){ nodes.add(node) }
    fun remove(node: Node){ nodes.remove(node) }
    fun reset(){ found = 0; done = 0 }
    override fun toString(): String{ return "$id: ($found, $done)" }
}

class Graph (n: Int){
    private var nodes = Array(n) { Node(it) }

    fun connect(a: Int, b: Int){ nodes[a].connect(nodes[b])}

    fun findStrongComponents() {
        var combinedTime: Long = 0
        var strongComponentsLinkedList: LinkedList<LinkedList<Node>>? = null
        measureTimeMillis { DFS() }.also { println("1-st DFS took $it milliseconds") }.also { combinedTime += it }
        //   println(toString())
        measureTimeMillis { nodes.sortByDescending { it.done } }.also { println("Sorting took $it milliseconds") }.also { combinedTime += it }
        measureTimeMillis { transpose() }.also { println("Transpose took $it milliseconds") }.also { combinedTime += it }
        measureTimeMillis { strongComponentsLinkedList = DFS() }.also { println("seconds DFS took $it milliseconds") }.also { combinedTime += it }

        if(nodes.size < 100) strongComponents(strongComponentsLinkedList!!)
        println("Strong comps: ${strongComponentsLinkedList!!.size}")
        println("Total time: $combinedTime milliseconds")

        /*  strongComponentsLinkedList!!.forEach {
              println(it.map(Node::id))
          }*/
    }

    private fun strongComponents(nodeLinkedList: LinkedList<LinkedList<Node>>){
        for(lList in nodeLinkedList){
            for(node in lList){
                println(node)
            }
            println("____________")
        }
    }

    private fun DFS(): LinkedList<LinkedList<Node>> {
        var counter = 0
        nodes.forEach { it.reset() }
        val s = Stack<Node>()
        val links = LinkedList<LinkedList<Node>>()
        for(node in nodes){
            if (node.found != 0) continue
            var n: Node? = node
            var link = LinkedList<Node>()
            while(s.count() > 0 || n != null){
                if(n!!.found == 0){
                    link.add(n)
                    n.found = ++counter
                }
                var next: Node? = n.nodes.firstOrNull{ it.found == 0 }

                if(next != null){
                    s.push(n)
                    n = next
                }
                else{
                    n.done = ++counter
                    n = if (s.count() > 0) s.pop() else null
                }
            }
            links.add(link)
        }
        return links
    }

    /* private fun transpose(){
         var transposedNodes = HashMap<Node, ArrayList<Node>>()
         transposedNodes.putAll(nodes.map { it to arrayListOf<Node>() })
         nodes.forEach {
             for(i in it.nodes.count() - 1 downTo 0){
                 if(!transposedNodes[it.nodes[i]]!!.contains(it)){
                     transposedNodes[it]!!.add(it.nodes[i])
                     it.nodes[i].connect(it)
                     it.remove(it.nodes[i])
                 }
                 else{
                     transposedNodes[it.nodes[i]]!!.remove(it)
                 }
             }
         }
     }*/

    fun transpose() {
        Arrays.stream(nodes)
                .parallel()
                .forEach {
                    it.nodes.forEach { neighbour ->
                        neighbour.temp.add(it)
                    }
                }
        nodes.forEach { node -> node.nodes = node.temp }

    }

    override fun toString(): String {
        var output = ""
        nodes.forEach {
            output += "\n${it} \t"
            for(c in it.nodes){
                output += c.id.toString() + ", "
            }
        }
        return output
    }
}

fun main(args: Array<String>){
    val paths = arrayListOf(
            "C:\\Users\\Aleks\\Documents\\algdatFiles\\assignment7\\L7g1.txt",
            "C:\\Users\\Aleks\\Documents\\algdatFiles\\assignment7\\L7g2Fixed.txt",
            "C:\\Users\\Aleks\\Documents\\algdatFiles\\assignment7\\L7g5.txt",
            "C:\\Users\\Aleks\\Documents\\algdatFiles\\assignment7\\L7g6.txt",
            "C:\\Users\\Aleks\\Documents\\algdatFiles\\assignment7\\L7g3.txt",
            "C:\\Users\\Aleks\\Documents\\algdatFiles\\assignment7\\L7Skandinavia"
    )
    val run = false
    if(run){
        for(path in paths){
            val files = File(path).bufferedReader()
            val nodes = files.readLine().split(" ", "\t")[0].toInt()

            val g = Graph(nodes)
            val length = path.split("\\").size - 1

            val regex = Regex("(\\d+)")
            measureTimeMillis {
                files.lines()
                        .parallel()
                        .forEach {
                            regex.findAll(it)
                                    .also { parts ->
                                        g.connect(parts.elementAt(0).value.toInt(), parts.last().value.toInt())
                                    }
                        }
            }.also {
                println("Graph ${path.split("\\")[length]} init took $it milliseconds")
            }
            g.findStrongComponents()
            println("\n-------------------------------------------------------\n")
        }
    }
    else{
        val path = "C:\\Users\\TDAT1337\\Documents\\GitHub\\Kotlin\\Oppgaver\\src\\algoritmerOgDatastrukturer\\Assets\\L7Skandinavia.txt"
        val files = File(path).bufferedReader()
        val nodes = files.readLine().split(" ", "\t")[0].toInt()

        val g = Graph(nodes)
        val length = path.split("\\").size - 1

        val regex = Regex("(\\d+)")
        measureTimeMillis {
            files.lines()
                    .parallel()
                    .forEach {
                        regex.findAll(it)
                                .also { parts ->
                                    g.connect(parts.elementAt(0).value.toInt(), parts.last().value.toInt())
                                }
                    }
        }.also {
            println("Graph ${path.split("\\")[length]} init took $it milliseconds")
        }
        g.findStrongComponents()
        println("\n-------------------------------------------------------\n")
    }
}
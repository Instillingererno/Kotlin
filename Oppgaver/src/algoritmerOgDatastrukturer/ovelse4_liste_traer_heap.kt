package algoritmerOgDatastrukturer

import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import javafx.stage.Stage
import org.graphstream.graph.Edge
import org.graphstream.graph.Graph
import org.graphstream.graph.implementations.SingleGraph
import java.lang.StringBuilder
import java.util.function.Consumer


fun main(args: Array<String>) {
    //println(dobbelLenkeOf("0000000001"))

    Application.launch(App::class.java, *args)
}


class DobbelLenke {
    private var size = 0
    private var head: Node? = null
    private var tail: Node? = null
    private var positiv = true

    fun size() = size

    fun negate() { positiv = !positiv }

    fun addFirst(element: Byte) {
        val temp = Node(element, head, null)
        if (head != null) head!!.forrige = temp
        head = temp
        if (tail == null) tail = temp
        size++
    }

    fun addLast(element: Byte) {
        val temp = Node(element, null, tail)
        if (tail != null) tail!!.neste = temp
        tail = temp
        if (head == null) head = temp
        size++
    }

    fun ascendingIterator(function: Consumer<Byte>) {
        var temp = head
        while (temp != null) {
            function.accept(temp.siffer)
            temp = temp.neste
        }
    }

    fun getFromLast(index: Int): Byte {
        if (index >= size) return 0
        var temp = tail
        for(i in 0 until index) temp = temp!!.forrige
        return temp!!.siffer
    }

    override fun toString() = StringBuilder()
                .also { output ->
                    if (!positiv) output.append("-")
                    ascendingIterator(Consumer { output.append(it) })
                }.toString()

    fun add(list: DobbelLenke): DobbelLenke {
        var carry = 0
        var nextNr: Byte = 0
        val largestSize = setOf(size, list.size()).max()!!
        val output = DobbelLenke()
        for (i in 0..largestSize) {
            if (i == largestSize && carry == 0) break
            nextNr = nextNr.plus(carry).plus(getFromLast(i)).plus(list.getFromLast(i)).toByte()
            carry = 0
            when (nextNr) {
                in 0..9 -> output.addFirst(nextNr)
                else -> {
                    carry++
                    output.addFirst(nextNr.minus(10).toByte())
                }
            }
            nextNr = 0
        }
        return output
    }

    fun subtract(list: DobbelLenke): DobbelLenke {
        var carry = 0
        var nextNr: Byte = 0
        val largestSize = setOf(size, list.size()).max()!!
        val output: DobbelLenke = if (list.size() > size) list.subtract(this).also { it.negate() } else {
            DobbelLenke().also {
                for (i in 0 until largestSize) {
                    nextNr = nextNr.plus(getFromLast(i)).minus(carry).minus(list.getFromLast(i)).toByte()
                    carry = 0
                    when (nextNr) {
                        in 0..9 -> it.addFirst(nextNr)
                        else -> {
                            carry--
                            it.addFirst(nextNr.plus(10).toByte())
                        }
                    }
                    nextNr = 0
                }
            }
        }
        return output
    }

    class Node(var siffer: Byte, var neste: Node?, var forrige: Node?)
}

fun dobbelLenkeOf(vararg elements: Byte) = DobbelLenke().also { lenke -> elements.forEach { lenke.addLast(it) } }


fun dobbelLenkeOf(tall: String) = dobbelLenkeOf(*tall.toCharArray()
        .filter { it.isDigit() }
        .map(Character::getNumericValue)
        .dropWhile { it == 0 }
        .map(Int::toByte).toByteArray())






class BinaertTre {
    var root: Node? = null

    fun add(ord: String) {
        if (root == null) root = Node(ord)
        else {
            root!!.add(ord)
        }
    }

    override fun toString(): String {
        return if (root == null) "No root" else root!!.pprint()
    }

    class Node(var ord: String, var left: Node? = null, var right: Node? = null) {

        fun add(ord: String) {
            if (ord.compareTo(this.ord, ignoreCase = true) < 0) {
                if (left == null) left = Node(ord)
                else left!!.add(ord)
            } else {
                if (right == null) right = Node(ord)
                else right!!.add(ord)
            }
        }

        fun pprint(indent: Int = 1): String {
            val y: String = "   ".repeat(indent)
            val leftString = if (left == null) "Null" else left!!.pprint(indent + 1)
            val rightString = if (right == null) "Null" else right!!.pprint(indent + 1)
            return StringBuilder().also { it.append(ord)
                    .append("\n ${y} ${leftString}")
                    .append("\n ${y} ${rightString}")
            }.toString()
        }
    }
}

fun binaertTreOf(tekst: String) = BinaertTre().also { tre -> tekst.trim().split(" ").filter(String::isNotEmpty).forEach { tre.add(it) } }



class App : Application() {
    override fun start(stage: Stage) {
        val edit = TextField()
        val tree = Canvas(400.0,600.0)
        val gc = tree.graphicsContext2D

        val vBox = VBox(edit, tree).apply {
            spacing = 15.0
            padding = Insets(15.0)
        }

        edit.textProperty().addListener { _ ->
            // POPULATE CANVAS
            println(binaertTreOf(edit.text))
        }

        stage.scene = Scene(vBox)
        stage.show()
    }
}
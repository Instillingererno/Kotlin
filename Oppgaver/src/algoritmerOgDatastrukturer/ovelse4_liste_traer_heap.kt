package algoritmerOgDatastrukturer

import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.lang.StringBuilder
import java.util.function.Consumer
import java.util.stream.DoubleStream


fun main(args: Array<String>) {
    val five = dobbelLenkeOf("5")
    val three = dobbelLenkeOf("3")

    //println(five.apply { negate() }.subtract(three).apply { negate() })

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

    private fun trim() {
        var temp = head
        while (temp != null && temp.siffer.toInt() == 0) {
            temp = temp.neste
        }
        if (temp != null) {
            if (temp!!.forrige != null) {
                temp.forrige.apply { this!!.neste = null }
                temp.forrige = null
                head = temp
            }
        } else {
            head = Node(0)
        }
    }

    override fun toString() = StringBuilder()
                .also { output ->
                    if (!positiv) output.append("-")
                    ascendingIterator(Consumer { output.append(it) })
                }.toString()


    fun add(list: DobbelLenke): DobbelLenke {
        /* if(!positiv) {
            when (list.positiv) {
                true -> return list.subtract(this.apply { negate() })
                false -> return apply { negate() }.add(list.apply { negate() })
            }
        } else if (!list.positiv) return subtract(list.apply { negate() }) */
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
        /* if(!positiv) {
            when (list.positiv) {
                true -> return list.apply { negate() }.subtract(this.apply { negate() })
                false -> return this.apply { negate() }.add(list.apply { negate() }).apply { negate() } // Denne blir feil, av ein eller annen grunn
            }
        } else if (!list.positiv) return this.add(list.apply { negate() }) */
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
                            carry++
                            it.addFirst(nextNr.plus(10).toByte())
                        }
                    }
                    nextNr = 0
                }
            }
        }
        output.trim()
        if (output.head == null) output.addFirst(0)

        return output
    }

    class Node(var siffer: Byte, var neste: Node? = null, var forrige: Node? = null)
}

fun dobbelLenkeOf(vararg elements: Byte) = DobbelLenke().also { lenke -> elements.forEach { lenke.addLast(it) } }

fun dobbelLenkeOf(tall: String) = dobbelLenkeOf(*tall.toCharArray()
        .filter(Char::isDigit)
        .map { it.toByte() }
        .dropWhile { it == 0.toByte() }
        .toByteArray())






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
            return StringBuilder().apply {
                    append(ord)
                    append("\n ${y} ${leftString}")
                    append("\n ${y} ${rightString}")
            }.toString()
        }
    }
}

fun binaertTreOf(tekst: String) = BinaertTre().also { tre -> tekst.trim().split(" ").filter(String::isNotEmpty).forEach { tre.add(it) } }



class App : Application() {
    val CANVAS_HEIGHT = 600.0
    val CANVAS_WIDTH = 1000.0
    val ovalWidth = 40.0
    val startX = CANVAS_WIDTH / 2 - ovalWidth / 2
    val startY = 50.0
    val angle = Math.toRadians(80.0)
    val angleLoss = Math.toRadians(20.0)
    val length = 150.0

    override fun start(stage: Stage) {
        val edit = TextField()
        val tree = Canvas(CANVAS_WIDTH, CANVAS_HEIGHT)
        val gc = tree.graphicsContext2D

        val vBox = VBox(edit, tree).apply {
            spacing = 15.0
            padding = Insets(15.0)
        }


        edit.textProperty().addListener { _ ->
            // POPULATE CANVAS
            gc.clearRect(0.0,0.0, CANVAS_WIDTH, CANVAS_HEIGHT)
            recursiveDraw(binaertTreOf(edit.text).root, gc, startX, startY, angle)
        }

        stage.scene = Scene(vBox)
        stage.show()
    }

    fun recursiveDraw(node: BinaertTre.Node?, gc: GraphicsContext, x: Double, y: Double, angle: Double) {
        if (node == null)  {
            gc.fillText("No node", x, y)
            return
        }
        drawOvalFromOrigin(gc, x, y)
        gc.fillText(node.ord, x, y)
        val dX = Math.sin(angle) * length
        val dY = Math.cos(angle) * length

        if (node.left != null) {
            gc.strokeLine(x + ovalWidth / 2,y + ovalWidth / 2, x - dX + ovalWidth / 2, y + dY + ovalWidth / 2)
            recursiveDraw(node.left!!, gc, x - dX, y + dY, angle - angleLoss)
        }
        if (node.right != null) {
            gc.strokeLine(x + ovalWidth / 2,y + ovalWidth / 2, x + dX + ovalWidth / 2, y + dY + ovalWidth / 2)
            recursiveDraw(node.right!!, gc, x + dX, y + dY, angle - angleLoss)
        }
    }

    fun drawOvalFromOrigin(gc: GraphicsContext, x: Double, y: Double) { gc.fillOval(x, y, ovalWidth, ovalWidth) }
}
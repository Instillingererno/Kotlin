package guiEksempler

import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

// WORDS: //Resources/ngrams.txt

class App : Application() {
    val ngrams = TreeSet<NGram>()

    override fun start(stage: Stage) {
        load()

        val edit = TextField()
        val completionsList = ListView<String>()

        val vBox = VBox(edit, completionsList).apply {
            spacing = 15.0
            padding = Insets(15.0)
        }

        edit.textProperty().addListener { _ ->
            completionsList.items.setAll(ngrams.complete(edit.text))
        }

        stage.scene = Scene(vBox)
        stage.show()
    }

    private fun load() {
        val path = Paths.get("Oppgaver/Resources/ngrams.txt")
        try {
            Files.lines(path, Charsets.UTF_8).forEach { line ->
                val words = line.substringAfter('\t').replace('\t',' ').toLowerCase()
                val freq = line.substringBefore('\t').toInt()
                val ngram = NGram(words, freq)
                ngrams.add(ngram)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

data class NGram(val words: String, val freq: Int = -1) : Comparable<NGram> {
    override fun compareTo(other: NGram): Int {
        val x = words.compareTo(other.words)
        return if (x != 0 || freq == -1) x
                else freq.compareTo(other.freq)
    }
}

fun String.pairwise() = mapIndexed { index, char -> if (index + 1 <= lastIndex) "$char${this[index + 1]}" else "" }.dropLast(1)

fun NavigableSet<NGram>.complete(input: String): List<String> {
    // Convert input into a sequence of pairs of all possible prefixes and suffixes
    //
    // "a vect" -> [
    //    a vect :
    //    a vec  : t
    //    a ve   : ct
    //    and so on
    // ]

    val canonicalisedInput = input.toLowerCase()
    val seq = (canonicalisedInput.length downTo 0).asSequence().map { // Sequence i kotlin e ein lazy-list
        canonicalisedInput.substring(0, it) to canonicalisedInput.substring(it)
    }

    fun NavigableSet<NGram>.query(s: String) = tailSet(NGram(s)).headSet(NGram(s + Char.MAX_SURROGATE))

    val queried = seq.map { query(it.first) to it.second }

    val matches = queried.dropWhile { it.first.isEmpty() }
    val (candidates, remaining) = matches.first()

    val charPairs = if (remaining.length == 1) listOf(remaining) else remaining.pairwise()
    val prefix = input.substring(0, input.length - remaining.length)

    return candidates.filter { c ->
        val postfix = c.words.substring(prefix.length)
        charPairs.map { pair ->
            when {
                pair.length == 1 -> postfix.contains(pair)
                // 'let' lets you calculate once and compare several times
                else -> postfix.indexOf(pair[0]).let { it > 0 && it < postfix.lastIndexOf(pair[1]) }
            }
        }.all { it }
    }.sortedByDescending { it.freq }.map { it.words }
}

// * spreads an array
fun main(args: Array<String>) = Application.launch(App::class.java, *args)

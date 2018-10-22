package algoritmerOgDatastrukturer


fun main(args: Array<String>) {
    val setA = setOf('a','x','r','m','2','0')
    val rel1 = setOf(
        charArrayOf('a', 'a'),
        charArrayOf('r', 'a'),
        charArrayOf('a', '2'),
        charArrayOf('x', 'x'),
        charArrayOf('r', '2'),
        charArrayOf('r', 'r'),
        charArrayOf('m', 'm'),
        charArrayOf('2', 'r'),
        charArrayOf('0', '0'),
        charArrayOf('a', 'r'),
        charArrayOf('2', '2'),
        charArrayOf('2', 'a')
    )
    val rel2 = setOf(
        charArrayOf('a', 'x'),
        charArrayOf('r', '2'),
        charArrayOf('0', '0'),
        charArrayOf('m', '2')
    )

    println("Rel1 is reflexive: " + isReflexive(rel1, setA))
    println("Rel2 is reflexive: " + isReflexive(rel2, setA))
    println("Rel1 is symmetric: " + isSymmetric(rel1, setA))
    println("Rel2 is symmetric: " + isSymmetric(rel2, setA))
    println()
    println("Reasdasdasd" + isSymmetric(setOf(charArrayOf('a', 'b'), charArrayOf('b', 'a'), charArrayOf('b', 'c')), setOf('a','b')))
    println("Rel1 is transitive: " + isTransitive(rel1, setA))
    println("Rel2 is transitive: " + isTransitive(rel2, setA))
    println("Rel1 is antisymmetric: " + isAntiSymmetric(rel1, setA))
    println("Rel2 is antisymmetric: " + isAntiSymmetric(rel2, setA))
    println("Rel1 is an equivalence relation: " + isEquivalenceRelation(rel1, setA))
    println("Rel2 is an equivalence relation: " + isEquivalenceRelation(rel2, setA))
    println("Rel1 is a partial order: " + isPartialOrder(rel1, setA))
    println("Rel2 is a partial order: " + isPartialOrder(rel2, setA))
    /* skal gi følgende utskrift:
       Rel1 is reflexive: true
       Rel2 is reflexive: false
       Rel1 is symmetric: true
       Rel2 is symmetric: false
       Rel1 is transitive: true
       Rel2 is transitive: true
       Rel1 is antisymmetric: false
       Rel2 is antisymmetric: true
       Rel1 is an equivalence relation: true
       Rel2 is an equivalence relation: false
       Rel1 is a partial order: false
       Rel2 is a partial order: false
     */



}

fun isReflexive(relation: Set<CharArray>, set: Set<Char>) =
    set.mapNotNull { relation.filter { char -> char[0] == it && char[1] == it }
                        .takeIf(Collection<CharArray>::isNotEmpty)?.let { true } }
            .takeIf { it.size == set.size }
            ?.let { true } ?: false

fun isSymmetric(relation: Set<CharArray>, set: Set<Char>) =
    set.mapNotNull { char ->
        relation.filter { it[0] == char || it[1] == char }
                .let {
                    for (value in it) {
                        when (value[0]) {
                            char -> if (relation.filter { v -> v[0] == value[1] && v[1] == char }.isNotEmpty()) return@let true
                            else -> if (relation.filter { v -> v[0] == char && v[1] == value[0] }.isNotEmpty()) return@let true
                        }
                    }
                    null
                } }
            .takeIf { it.size == set.size }
            ?.let { true } ?: false


fun isTransitive(relation: Set<CharArray>, set: Set<Char>) =
    set.mapNotNull { char ->
        relation.filter { it[0] == char }.let { rel ->
            val temp = rel.map { it[1].toInt() }
            relation.filter { temp.contains(it[0].toInt()) }.also { rel2 ->
                val temp2 = rel2.map { it[1].toInt() }
                return@let relation.filter { temp2.contains(it[0].toInt()) }
                        .filter { it[1] == char }
                        .takeIf(Collection<CharArray>::isNotEmpty)?.let { true }
            }
        }
    }.takeIf { it.size == set.size }?.let { true } ?: false

fun isAntiSymmetric(relation: Set<CharArray>, set: Set<Char>) =
    set.mapNotNull { char ->
        relation.filter { (it[0] == char || it[1] == char) && it[0] != it[1] }
                .let {
                    for (value in it) {
                        when (value[0]) {
                            char -> if (relation.filter { v -> v[0] == value[1] && v[1] == char }.isNotEmpty()) return@let true
                            else -> if (relation.filter { v -> v[0] == char && v[1] == value[0] }.isNotEmpty()) return@let true
                        }
                    }
                    null
                } }
            .takeIf { it.isEmpty() }
            ?.let { true } ?: false

fun isEquivalenceRelation(relation: Set<CharArray>, set: Set<Char>) =
        isReflexive(relation, set) && isSymmetric(relation, set) && isReflexive(relation, set)

fun isPartialOrder(relation: Set<CharArray>, set: Set<Char>): Boolean {


    TODO()
}





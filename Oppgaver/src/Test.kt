fun main(args: Array<String>) {

    val tekst = "Dette er en god dag for en kopp med kaffe"

    //Regex("([a-zA-Z])").findAll(tekst).groupingBy { it.value }.eachCount().also { println(it) }


    addOne()

}


tailrec fun addOne(x: Int = 0) {
    if (x == 1_000_000_000) println(x)
    else addOne(x + 1)
}
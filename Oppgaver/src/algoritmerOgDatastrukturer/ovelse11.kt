package algoritmerOgDatastrukturer

class Automat(val inputAlfabet: CharArray, val acceptState: IntArray, val nextState: Array<IntArray>) {
    fun sjekkInput(input: CharArray): Boolean {
        var currentState = 0
        for (i in input) { currentState = nextState(currentState, i) }
        return acceptState.contains(currentState)
    }

    fun nextState(fromState: Int, input: Char) = nextState[fromState][inputAlfabet.indexOf(input)]
}



fun main(args: Array<String>) {
    Automat(charArrayOf('0', '1'), intArrayOf(2), arrayOf(
            intArrayOf(1, 3),
            intArrayOf(1, 2),
            intArrayOf(2, 3),
            intArrayOf(3, 3)
    )).also {
        println("""
            Test på 3a.
            "":         ${it.sjekkInput(charArrayOf())}
            "010":      ${it.sjekkInput("010".toCharArray())}
            "111":      ${it.sjekkInput("111".toCharArray())}
            "010110":   ${it.sjekkInput("010110".toCharArray())}
            "0010000":  ${it.sjekkInput("0010000".toCharArray())}
        """.trimIndent())
    }

    Automat(charArrayOf('a', 'b'), intArrayOf(3), arrayOf(
            intArrayOf(1, 2),
            intArrayOf(4, 3),
            intArrayOf(3, 4),
            intArrayOf(3, 3),
            intArrayOf(4, 4)
    )).also {
        println("""
            Test på 3b.
            "abbb":     ${it.sjekkInput("abbb".toCharArray())}
            "aaab":     ${it.sjekkInput("aaab".toCharArray())}
            "babab":    ${it.sjekkInput("babab".toCharArray())}
        """.trimIndent())
    }
}
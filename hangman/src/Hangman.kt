

fun main(args : Array<String>) {
    println("Hello, world!")

    val name = "Sveinung Øverland" // Immutable variable

    var age = 20 // Mutable variable

    var bigInt: Int = Int.MAX_VALUE // Variable declaration with type
    var smallInt: Int = Int.MIN_VALUE // Variable declaration with type

    println("Biggest Int : $bigInt")
    println("Smalles Int : $smallInt")
    println("Difference : ${bigInt - smallInt}")

    // Types: Long - Double - Float - Boolean - Short - Byte - Char

    // For type checking use 'is' f.eks: 'if (true is Boolean)'

    // Casting
    println("3.14 to Int: ${3.14 to Int}")

    val myName = "Sveinung Øverland"

    val longStr = """This is a
        long string"""

    var fName: String = "Potato"
    var lName: String = "Salad"

    
}
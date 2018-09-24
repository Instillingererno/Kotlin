import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONWriter
import java.io.File
import java.util.*


fun main(args: Array<String>) {
    val onsdag = File("C:\\Users\\TDAT1337\\Documents\\GitHub\\Kotlin\\Oppgaver\\src\\KdJSON\\Onsdag.json").readText()
    val onsdagArray = JSONArray(onsdag)

    val torsdag = File("C:\\Users\\TDAT1337\\Documents\\GitHub\\Kotlin\\Oppgaver\\src\\KdJSON\\Torsdag.json").readText()
    val torsdagArray = JSONArray(torsdag)


    /*
        val onsdagWriter = File("C:\\Users\\KALAM\\Documents\\Kotlin\\Oppgaver\\src\\KdJSON\\OnsdagClean.json").writer()
        val torsdagWriter = File("C:\\Users\\KALAM\\Documents\\Kotlin\\Oppgaver\\src\\KdJSON\\TorsdagClean.json").writer()
        val coursesWriter = File("C:\\Users\\KALAM\\Documents\\Kotlin\\Oppgaver\\src\\KdJSON\\Courses.json").writer()
        val sectorsWriter = File("C:\\Users\\KALAM\\Documents\\Kotlin\\Oppgaver\\src\\KdJSON\\Sectors.json").writer()
    */
    val wantedValues = arrayOf("name", "no_text", "attending_onsdag", "attending_torsdag", "sectors", "courses", "attending_kk", "attending_ip")

    val mapValues = mapOf(
            "name" to "name",
            "no_text" to "description",
            "sectors" to "sectors",
            "courses" to "courses",
            "attending_kk" to "karriereKveld",
            "attending_ip" to "internshipPresentasjon",
            "attending_onsdag" to "onsdag",
            "attending_torsdag" to "torsdag"
    )
    val totalSectorsSet = mutableSetOf<String>()
    val totalCoursesSet = mutableSetOf<String>()


    fun mapToClean(jsonArray: JSONArray): JSONArray {
        return JSONArray(jsonArray.mapNotNull { item -> when(item) {
            is JSONObject -> {
                JSONObject().also { output ->
                    wantedValues.forEach { output.put(mapValues[it], item.get(it).takeUnless { value -> it == "no_text" && value is JSONArray}?.also { value -> when(it) {
                        "sectors" -> (value as JSONArray).forEach { sector -> totalSectorsSet.add(sector as String) }
                        "courses" -> (value as JSONArray).forEach { course -> totalCoursesSet.add(course as String) }
                    } } ?: item.get("en_text") ) }
                }
            }
            else -> null
        } })
    }
    val test1 = JSONObject("{'key': 'value'}")
    val test2 = JSONObject("{'key': 'value'}")

    //println(test1.similar(test2))

    val cleanOnsdag = mapToClean(onsdagArray)
    val cleanTorsdag = mapToClean(torsdagArray)

    val cleanCombination = cleanOnsdag.union(cleanTorsdag)

    //println(cleanCombination)

    val totalCourses = JSONArray(totalCoursesSet.toTypedArray())
    val totalSectors = JSONArray(totalSectorsSet.toTypedArray())


    println("""
        size: ${cleanOnsdag.length()}

        {
            "onsdag": $cleanOnsdag,

            "torsdag": $cleanTorsdag,

            "courses": $totalCourses,

            "sectors": $totalSectors

        }
    """.trimIndent())

}
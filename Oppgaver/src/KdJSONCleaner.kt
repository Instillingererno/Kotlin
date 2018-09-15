import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONWriter
import java.io.File
import java.util.*


fun main(args: Array<String>) {
    val onsdag = File("C:\\Users\\KALAM\\Documents\\Kotlin\\Oppgaver\\src\\KdJSON\\Onsdag.json").readText()
    val onsdagArray = JSONArray(onsdag)

    val torsdag = File("C:\\Users\\KALAM\\Documents\\Kotlin\\Oppgaver\\src\\KdJSON\\Torsdag.json").readText()
    val torsdagArray = JSONArray(torsdag)

    val onsdagWriter = File("C:\\Users\\KALAM\\Documents\\Kotlin\\Oppgaver\\src\\KdJSON\\OnsdagClean.json").writer()
    val torsdagWriter = File("C:\\Users\\KALAM\\Documents\\Kotlin\\Oppgaver\\src\\KdJSON\\TorsdagClean.json").writer()
    val coursesWriter = File("C:\\Users\\KALAM\\Documents\\Kotlin\\Oppgaver\\src\\KdJSON\\Courses.json").writer()
    val sectorsWriter = File("C:\\Users\\KALAM\\Documents\\Kotlin\\Oppgaver\\src\\KdJSON\\Sectors.json").writer()

    val wantedValues = arrayOf("name", "no_text", "sectors", "courses")
    val totalSectorsSet = mutableSetOf<String>()
    val totalCoursesSet = mutableSetOf<String>()

    fun mapToClean(jsonArray: JSONArray): JSONArray {
        return JSONArray(jsonArray.mapNotNull { item -> when(item) {
            is JSONObject -> {
                JSONObject().also { output ->
                    wantedValues.forEach { output.put(it, item.get(it).also { value -> when(it) {
                        "sectors" -> (value as JSONArray).forEach { sector -> totalSectorsSet.add(sector as String) }
                        "courses" -> (value as JSONArray).forEach { course -> totalCoursesSet.add(course as String) }
                    } }) }
                }
            }
            else -> null
        } })
    }

    val cleanOnsdag = mapToClean(onsdagArray)
    val cleanTorsdag = mapToClean(torsdagArray)

    val totalCourses = JSONArray(totalCoursesSet.toTypedArray())
    val totalSectors = JSONArray(totalSectorsSet.toTypedArray())

    println("CleanOnsdag: \n${cleanOnsdag}")
    println("\n\n\n\n\n\n\n\n\n")
    println("CleanTorsdag: \n${cleanTorsdag}")
    println("\n\n\n\n\n\n\n\n\n")
    println("Courses: \n${totalCourses}")
    println("\n\n\n\n\n\n\n\n\n")
    println("Sectors: \n${totalSectors}")

}
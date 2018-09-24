import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONWriter
import java.io.File
import java.util.*
import java.util.stream.Stream
import kotlin.streams.toList


fun main(args: Array<String>) {
    val onsdag = File("C:\\Users\\TDAT1337\\Documents\\GitHub\\Kotlin\\Oppgaver\\src\\KdJSON\\Onsdag.json").readText()
    val onsdagArray = JSONArray(onsdag)

    val torsdag = File("C:\\Users\\TDAT1337\\Documents\\GitHub\\Kotlin\\Oppgaver\\src\\KdJSON\\Torsdag.json").readText()
    val torsdagArray = JSONArray(torsdag)

    val nameUrl = File("C:\\Users\\KALAM\\Documents\\Kotlin\\Oppgaver\\src\\KdJSON\\nameUrlMap.json").readText()
    val nameUrlArray = JSONArray(nameUrl)

    val onsdagWriter = File("C:\\Users\\KALAM\\Documents\\Kotlin\\Oppgaver\\src\\KdJSON\\OnsdagClean.json").writer()
    val torsdagWriter = File("C:\\Users\\KALAM\\Documents\\Kotlin\\Oppgaver\\src\\KdJSON\\TorsdagClean.json").writer()
    val coursesWriter = File("C:\\Users\\KALAM\\Documents\\Kotlin\\Oppgaver\\src\\KdJSON\\Courses.json").writer()
    val sectorsWriter = File("C:\\Users\\KALAM\\Documents\\Kotlin\\Oppgaver\\src\\KdJSON\\Sectors.json").writer()

    val wantedValues = arrayOf("name", "no_text", "sectors", "courses", "attending_onsdag", "attending_torsdag", "attending_kk", "attending_ip")
    val totalSectorsSet = mutableSetOf<String>()
    val totalCoursesSet = mutableSetOf<String>()


    val nameUrlMap = nameUrlArray.mapNotNull { when(it) {
                is JSONObject -> it.get("name") to it.get("url")
                else -> null
            } }.toMap()


    val wantedValuesMap = mapOf(
            "name" to "name",
            "no_text" to "description",
            "sectors" to "sectors",
            "courses" to "courses",
            "attending_onsdag" to "onsdag",
            "attending_torsdag" to "torsdag",
            "attending_ip" to "internPres",
            "attending_kk" to "karriereKveld"
    )


    fun mapToClean(jsonArray: JSONArray): List<JSONObject> {
        return jsonArray.mapNotNull { item -> when(item) {
            is JSONObject -> {
                JSONObject().also { output ->
                    wantedValues.forEach { output.put(wantedValuesMap[it], item.get(it)
                            .takeUnless { value -> it == "no_text" && value !is String }
                            ?.also { value -> when(it) {
                                "name" -> output.put("logo", nameUrlMap.getOrDefault(value, "no logo found") as String)
                                "sectors" -> (value as JSONArray).forEach { sector -> totalSectorsSet.add(sector as String) }
                                "courses" -> (value as JSONArray).forEach { course -> totalCoursesSet.add(course as String) }
                    } } ?: item.get("en_text")) }
                }
            }
            else -> null
        } }
    }
    val test1 = JSONObject("{'key': 'value'}")
    val test2 = JSONObject("{'key': 'value'}")

    val onsdagClean = mapToClean(onsdagArray)
    val torsdagClean = mapToClean(torsdagArray)


    val totalCourses = JSONArray(totalCoursesSet.toTypedArray())
    val totalSectors = JSONArray(totalSectorsSet.toTypedArray())

    val cleaned = onsdagClean.plus(torsdagClean).distinctBy { it.get("name") }.also {
        println(it.size)
    }


    println("""
        {
            companies: $cleaned,

            courses: $totalCourses,

            sectors: $totalSectors

        }
    """.trimIndent())

}
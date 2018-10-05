import org.json.JSONArray
import org.json.JSONObject
import java.io.File

fun main(args: Array<String>) {
    val onsdag = File("C:\\Users\\TDAT1337\\Documents\\GitHub\\Kotlin\\Oppgaver\\src\\KdJSON\\Onsdag.json").readText()
    val onsdagArray = JSONArray(onsdag)

    val torsdag = File("C:\\Users\\TDAT1337\\Documents\\GitHub\\Kotlin\\Oppgaver\\src\\KdJSON\\Torsdag.json").readText()
    val torsdagArray = JSONArray(torsdag)

    val nameUrl = File("C:\\Users\\KALAM\\Documents\\Kotlin\\Oppgaver\\src\\KdJSON\\nameUrlMap.json").readText()
    val nameUrlArray = JSONArray(nameUrl)

    // Attributes to collect from json objects
    val wantedValues = arrayOf("name", "no_text", "sectors", "courses", "attending_onsdag", "attending_torsdag", "attending_kk", "attending_ip")

    // INIT sectors and courses as mutable Sets; this won't allow copies to exist
    val totalSectorsSet = mutableSetOf<String>()
    val totalCoursesSet = mutableSetOf<String>()


    val test = arrayOf("asdasd", "asdasd")

    val mutListTEst = mutableListOf<Int>()

    val asdasdsda = HashMap<String, MutableList<Int>>().putAll(test.map { it to mutableListOf<Int>() })


    // INIT map that contains info on company name and their logo url
    val nameUrlMap = nameUrlArray.mapNotNull { when(it) {
                is JSONObject -> it.get("name") to it.get("url")
                else -> null
            } }.toMap()


    // INIT map that maps json attributes to wanted names
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


    // Function that takes jsonArray and maps over
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

    val onsdagClean = mapToClean(onsdagArray)
    val torsdagClean = mapToClean(torsdagArray)


    val totalCourses = JSONArray(totalCoursesSet.toTypedArray())
    val totalSectors = JSONArray(totalSectorsSet.toTypedArray())

    val cleaned = onsdagClean.plus(torsdagClean).distinctBy { it.get("name") }

    println("""
        {
            companies: $cleaned,

            courses: $totalCourses,

            sectors: $totalSectors

        }
    """.trimIndent())
}
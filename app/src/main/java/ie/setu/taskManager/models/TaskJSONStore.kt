package ie.setu.taskManager.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ie.setu.taskManager.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "tasks.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<TaskManagerModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class TaskJSONStore(private val context: Context) : TaskStore {

    var tasks = mutableListOf<TaskManagerModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<TaskManagerModel> {
        logAll()
        return tasks
    }

    override fun create(task: TaskManagerModel) {
        task.id = generateRandomId()
        tasks.add(task)
        serialize()
    }


    override fun update(task: TaskManagerModel) {
        // todo
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(tasks, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        tasks = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        tasks.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}
package ie.setu.taskManager.models


import timber.log.Timber.i

var lastId = 0L
internal fun getId() = lastId++

class TaskMemStore : TaskStore {

    val tasks = ArrayList<TaskManagerModel>()

    override fun findAll(): List<TaskManagerModel> {
        return tasks
    }

    override fun create(task: TaskManagerModel) {
        task.id = getId()
        tasks.add(task)
        logAll()
    }

    override fun update(task: TaskManagerModel) {
        var foundTask: TaskManagerModel? = tasks.find { p -> p.id == task.id }
        if (foundTask != null) {
            foundTask.title = task.title
            foundTask.description = task.description
            logAll()
        }
    }

    private fun logAll() {
        tasks.forEach { i("$it") }
    }
}
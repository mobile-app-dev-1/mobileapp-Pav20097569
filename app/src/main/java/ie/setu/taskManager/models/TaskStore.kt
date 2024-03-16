package ie.setu.taskManager.models

interface TaskStore {
    fun findAll(): List<TaskManagerModel>
    fun create(task: TaskManagerModel)
    fun update(task: TaskManagerModel)
}
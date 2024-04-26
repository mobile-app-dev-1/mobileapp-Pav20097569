package ie.setu.taskManager.main

import android.app.Application
import ie.setu.taskManager.models.TaskJSONStore
import ie.setu.taskManager.models.TaskMemStore
import ie.setu.taskManager.models.TaskManagerModel
import ie.setu.taskManager.models.TaskStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {
    // val tasks = ArrayList<TaskManagerModel>()
    // val tasks = TaskMemStore()
    lateinit var tasks: TaskStore
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
       // tasks = TaskMemStore()
        tasks = TaskJSONStore(applicationContext)
        i("Task started")
    }
}

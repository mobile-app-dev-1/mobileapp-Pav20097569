package ie.setu.taskManager.main

import android.app.Application
import ie.setu.taskManager.models.TaskMemStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

   // val tasks = ArrayList<TaskManagerModel>()
    val tasks = TaskMemStore()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Task started")
    }
}
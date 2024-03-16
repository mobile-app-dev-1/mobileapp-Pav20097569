package ie.setu.taskManager.main

import android.app.Application
import ie.setu.taskManager.models.TaskManagerModel
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    val tasks = ArrayList<TaskManagerModel>()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Task started")
    }
}
package ie.setu.taskManager.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import ie.setu.taskManager.databinding.ActivityMainBinding
import ie.setu.taskManager.models.TaskManagerModel
import timber.log.Timber
import timber.log.Timber.i

class TaskManagerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val tasks = ArrayList<TaskManagerModel>()
    val task = TaskManagerModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Timber.plant(Timber.DebugTree())
        i("Activity started...")

        tasks.add(task.copy())


        binding.btnAdd.setOnClickListener {
            task.title = binding.taskTitle.text.toString()
            if (task.title.isNotEmpty()) {
                tasks.add(task.copy()) // Add a copy of task to the tasks list
                i("Add button pressed: $task")
                for (i in tasks.indices) {
                    i("Task[$i]: ${tasks[i]}")
                }
            } else {
                Snackbar.make(it, "Please Enter a Title", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun logAllTasks() {
        i("All Tasks:")
        tasks.forEachIndexed { index, task ->
            Timber.i("Placemark ${index + 1}: Title - ${task.title}, Description - ${task.description}")
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}

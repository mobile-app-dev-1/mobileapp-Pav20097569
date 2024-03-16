package ie.setu.taskManager.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import ie.setu.taskManager.databinding.ActivityMainBinding
import ie.setu.taskManager.main.MainApp
import ie.setu.taskManager.models.TaskManagerModel
import timber.log.Timber.i
class TaskManagerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var task = TaskManagerModel()
    lateinit var app: MainApp


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp
        i("Task Activity started...")
        binding.btnAdd.setOnClickListener() {
            task.title = binding.taskTitle.text.toString()
            task.description = binding.taskDescription.text.toString()
            if (task.title.isNotEmpty()) {
                app.tasks.add(task.copy())
                i("add Button Pressed: ${task}")
                for (i in app.tasks.indices) {
                    i("task[$i]:${this.app.tasks[i]}")
                }
                setResult(RESULT_OK)
                finish()
            }
            else {
                Snackbar.make(it,"Please Enter a title", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}
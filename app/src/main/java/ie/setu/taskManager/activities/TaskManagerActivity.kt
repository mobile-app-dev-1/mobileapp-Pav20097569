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
    var task = TaskManagerModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Timber.plant(Timber.DebugTree())
        i("Activity started...")


        binding.btnAdd.setOnClickListener() {
            task.title = binding.taskTitle.toString()
            if (task.title.isNotEmpty()) {
                i("add Button Pressed: ${task.title}")
            }
            else {
                Snackbar
                    .make(it,"Please Enter a title", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}
package ie.setu.taskManager.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import ie.setu.taskManager.R
import ie.setu.taskManager.adapters.TaskAdapter
import ie.setu.taskManager.adapters.TaskListener
import ie.setu.taskManager.databinding.ActivityTaskListBinding
import ie.setu.taskManager.main.MainApp
import ie.setu.taskManager.models.TaskManagerModel

class TaskListActivity : AppCompatActivity(), TaskListener {
    private lateinit var app: MainApp
    private lateinit var binding: ActivityTaskListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using view binding
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the MainApp instance
        app = application as MainApp

        // Set up RecyclerView
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = TaskAdapter(app.tasks.findAll(), this)

        // Set toolbar title
        binding.topAppBar.title = title // Name of the Project
        setSupportActionBar(binding.topAppBar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                // Launch TaskManagerActivity when add item is clicked
                val launcherIntent = Intent(this, TaskManagerActivity::class.java)
                getResult.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val getResult =
        registerForActivityResult(
            // Register a callback for when TaskManagerActivity finishes
            ActivityResultContracts.StartActivityForResult(),
        ) {
            // Handle the result when TaskManagerActivity finishes
            if (it.resultCode == Activity.RESULT_OK) {
                // If the result is OK, notify adapter to update the RecyclerView
                (binding.recyclerView.adapter)?.notifyItemRangeChanged(
                    0,
                    app.tasks.findAll().size,
                )
            }
            if (it.resultCode == Activity.RESULT_CANCELED) {
                // If the result is CANCELED, show a Snackbar indicating cancellation
                Snackbar.make(binding.root, getString(R.string.task_add_cancelled), Snackbar.LENGTH_LONG).show()
            }
        }

    override fun onTaskClick(task: TaskManagerModel) {
        // Launch TaskManagerActivity with the selected task for editing
        val launcherIntent = Intent(this, TaskManagerActivity::class.java)
        launcherIntent.putExtra("task_edit", task)
        getResult.launch(launcherIntent)
    }
}

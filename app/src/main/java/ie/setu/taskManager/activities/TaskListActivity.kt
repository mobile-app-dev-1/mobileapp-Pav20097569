package ie.setu.taskManager.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
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
    private var position: Int = 0
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        drawerLayout = binding.drawerlayout
        navView = binding.navView

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding.topAppBar, // Pass the topAppBar reference here
            R.string.nav_open,
            R.string.nav_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState() // Synchronize the state of the toggle button with the drawer layout


        binding.topAppBar.title = title
        setSupportActionBar(binding.topAppBar) // Set the top app bar as the support action bar

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_account -> {
                    Toast.makeText(this, "Account Page", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }

        // Initialize the MainApp instance
        app = application as MainApp

        // Set up RecyclerView
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = TaskAdapter(app.tasks.findAll(), this)

        // Set toolbar title
        binding.topAppBar.title = title // Name of the Project
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
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
            when(it.resultCode) {
                Activity.RESULT_OK ->
                    (binding.recyclerView.adapter)?.notifyItemRangeChanged(
                        0,
                        app.tasks  .findAll().size)
                Activity.RESULT_CANCELED ->
                    Snackbar.make(
                        binding.root,
                        getString(R.string.task_add_cancelled), Snackbar.LENGTH_LONG).show()
                99 ->
                    (binding.recyclerView.adapter)?.notifyItemRemoved(position)
            }
        }










    override fun onTaskClick(task: TaskManagerModel, pos: Int) {
        // Launch TaskManagerActivity with the selected task for editing
        val launcherIntent = Intent(this, TaskManagerActivity::class.java)
        launcherIntent.putExtra("task_edit", task)
        position = pos
        getResult.launch(launcherIntent)
    }
}

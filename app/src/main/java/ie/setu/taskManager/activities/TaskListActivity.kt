package ie.setu.taskManager.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var adapter: TaskAdapter
    private var position: Int = 0

    private var allTasks: MutableList<TaskManagerModel> = mutableListOf()

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
            binding.topAppBar,
            R.string.nav_open,
            R.string.nav_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.topAppBar.title = title
        setSupportActionBar(binding.topAppBar)

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_settings -> {
                    Toast.makeText(this, "Settings Page", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, SettingsActivity::class.java))
                }

                R.id.nav_account -> {
                    Toast.makeText(this, "My Account Page Currently Not Available", Toast.LENGTH_SHORT).show()
                }

                R.id.nav_logout -> {
                    Toast.makeText(this, "Logout Not Available", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }

        // Initialize the adapter with an empty list initially
        adapter = TaskAdapter(allTasks, this)
        binding.recyclerView.adapter = adapter

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        adapter = TaskAdapter(allTasks, this) // Initialize your adapter with allTasks
        binding.recyclerView.adapter = adapter // Set the adapter to the RecyclerView


        // Load tasks from data source
        //loadTasks()

        // Set up search view
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { query ->
                    filterTasks(query)
                }
                return true
            }
        })


        loadTasks()
    }

    private fun loadTasks() {
        allTasks.clear()
        allTasks.addAll(app.tasks.findAll())
        adapter.notifyDataSetChanged()
    }

    private fun filterTasks(query: String) {
        val filteredTasks = allTasks.filter { task ->
            task.title.contains(query, ignoreCase = true) || task.description.contains(
                query,
                ignoreCase = true
            )
        }
        adapter.setFilteredList(filteredTasks)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, TaskManagerActivity::class.java)
                getResult.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }



    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            when(it.resultCode) {
                Activity.RESULT_OK ->
                 //   (binding.recyclerView.adapter)?.notifyItemRangeChanged(
                  //      0,
                    //    app.tasks.findAll().size)

                    loadTasks()

                Activity.RESULT_CANCELED ->
                    Snackbar.make(
                        binding.root,
                        getString(R.string.task_add_cancelled), Snackbar.LENGTH_LONG).show()
                99 -> {
                  //  (binding.recyclerView.adapter)?.notifyItemRemoved(position)
                    if(position != RecyclerView.NO_POSITION){
                        allTasks.removeAt(position)
                        adapter.notifyItemRemoved(position)
                    }
                }


            }
        }


    override fun onTaskClick(task: TaskManagerModel, pos: Int) {
        val launcherIntent = Intent(this, TaskManagerActivity::class.java)
        launcherIntent.putExtra("task_edit", task)
        position = pos
        getResult.launch(launcherIntent)
    }
}

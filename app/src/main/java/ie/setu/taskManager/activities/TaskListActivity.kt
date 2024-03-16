package ie.setu.taskManager.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.setu.taskManager.R
import ie.setu.taskManager.databinding.ActivityTaskListBinding
import ie.setu.taskManager.databinding.CardTaskBinding
import ie.setu.taskManager.main.MainApp
import ie.setu.taskManager.models.TaskManagerModel

class TaskListActivity : AppCompatActivity() {

    lateinit var app: MainApp
    private lateinit var binding: ActivityTaskListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = TaskAdapter(app.tasks)

        binding.topAppBar.title = title  //Name of the Project
        setSupportActionBar(binding.topAppBar)
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
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.tasks.size)
            }
        }
}

class TaskAdapter constructor(private var tasks: List<TaskManagerModel>) :
    RecyclerView.Adapter<TaskAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardTaskBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val task = tasks[holder.adapterPosition]
        holder.bind(task)
    }

    override fun getItemCount(): Int = tasks.size

    class MainHolder(private val binding : CardTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: TaskManagerModel) {
            binding.taskTitle.text = task.title
            binding.taskDescription.text = task.description
        }
    }
}
package ie.setu.taskManager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.setu.taskManager.databinding.CardTaskBinding
import ie.setu.taskManager.models.TaskManagerModel

// Interface for listening to task clicks
interface TaskListener {
    fun onTaskClick(task: TaskManagerModel)
}

// Adapter for the RecyclerView to display tasks
class TaskAdapter(
    // List of tasks to display
    private var tasks: List<TaskManagerModel>,
    // Listener for task clicks
    private val listener: TaskListener
) : RecyclerView.Adapter<TaskAdapter.MainHolder>() {
    // Create view holder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType:
        Int): MainHolder {
        // Inflate the layout for each item in the RecyclerView
        val binding = CardTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    // Bind data to view holder
    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task, listener)
    }

    // Get the number of items in the RecyclerView
    override fun getItemCount(): Int = tasks.size

    // View holder class
    class MainHolder(private val binding: CardTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        // Bind task data to views
        fun bind(task: TaskManagerModel, listener: TaskListener) {
            binding.taskTitle.text = task.title
            binding.taskDescription.text = task.description
            binding.taskDate.text = task.date // Make sure "task.date" is correctly referencing the date property
            binding.root.setOnClickListener { listener.onTaskClick(task) }
        }
    }
}

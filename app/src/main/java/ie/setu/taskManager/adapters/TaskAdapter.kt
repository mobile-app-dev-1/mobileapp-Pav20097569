package ie.setu.taskManager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.setu.taskManager.databinding.CardTaskBinding
import ie.setu.taskManager.models.TaskManagerModel

interface TaskListener {
    fun onTaskClick(task: TaskManagerModel)
}

class TaskAdapter(
    private var tasks: List<TaskManagerModel>,
    private val listener: TaskListener
) : RecyclerView.Adapter<TaskAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task, listener)
    }

    override fun getItemCount(): Int = tasks.size

    class MainHolder(private val binding: CardTaskBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: TaskManagerModel, listener: TaskListener) {
            binding.taskTitle.text = task.title
            binding.taskDescription.text = task.description
            binding.taskDate.text = task.date // Make sure "task.date" is correctly referencing the date property
            binding.root.setOnClickListener { listener.onTaskClick(task) }
        }
    }
}

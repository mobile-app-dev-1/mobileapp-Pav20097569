package ie.setu.taskManager.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.setu.taskManager.R
import ie.setu.taskManager.databinding.CardTaskBinding
import ie.setu.taskManager.models.TaskManagerModel

// Interface for listening to task clicks
interface TaskListener {
    fun onTaskClick(task: TaskManagerModel , position: Int)
}

// Adapter for the RecyclerView to display tasks
class TaskAdapter(private var tasks: List<TaskManagerModel>, private val listener: TaskListener) :
    RecyclerView.Adapter<TaskAdapter.MainHolder>() {



    fun setFilteredList(tasks: List<TaskManagerModel>){
        this.tasks = tasks
        notifyDataSetChanged()
    }

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
            binding.taskDate.text = task.date
            Picasso.get().load(task.image).resize(200,200).into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onTaskClick(task,adapterPosition) }
        }
    }
}

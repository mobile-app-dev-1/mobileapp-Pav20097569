package ie.setu.taskManager.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.setu.taskManager.R
import ie.setu.taskManager.databinding.ActivityMainBinding
import ie.setu.taskManager.helpers.showImagePicker
import ie.setu.taskManager.main.MainApp
import ie.setu.taskManager.models.TaskJSONStore
import ie.setu.taskManager.models.TaskManagerModel
import ie.setu.taskManager.models.TaskStore
import timber.log.Timber.i
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class TaskManagerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var selectedDateTextView: TextView
    private lateinit var searchView: SearchView
    private lateinit var searchList: ArrayList<TaskJSONStore>


    // Initialize a TaskManagerModel object
    private var task = TaskManagerModel()
    private lateinit var app: MainApp
    var edit = false //tracks if we arrived here via an existing task
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout using data binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set toolbar title
        binding.topAppBar.title = title
        setSupportActionBar(binding.topAppBar)

        // Initialize views
        selectedDateTextView = findViewById(R.id.taskDate)
        val chooseDateButton: Button = findViewById(R.id.btnDatePicker)

        // Set click listener for chooseDateButton
        chooseDateButton.setOnClickListener { showCalendarPicker() }

        // Initialize app and task
        app = application as MainApp

        // Log activity start
        i(getString(R.string.task_activity_started))

        if (intent.hasExtra("task_edit")) {
            edit = true
            task = intent.extras?.getParcelable("task_edit")!!
            binding.taskTitle.setText(task.title)
            binding.taskDescription.setText(task.description)
            binding.taskDate.setText(task.date)
            binding.btnAdd.text = getString(R.string.save_task)
            if (task.image.path != null && task.image.path!!.isNotEmpty()) {
                // Uri is not empty, load the image
                Picasso.get().load(task.image).into(binding.taskImage)
            } else {
                // Uri is empty or null, load default image
                Picasso.get().load(R.drawable.default_image).into(binding.taskImage)
            }

        }



        // Set click listener for add button
        binding.btnAdd.setOnClickListener() {
            task.title = binding.taskTitle.text.toString()
            task.description = binding.taskDescription.text.toString()
            task.date = selectedDateTextView.text.toString() // Pass selected date
           // task.date = binding.taskDate.text.toString()
            if (task.title.isNotEmpty()) {
                if (edit) {
                    app.tasks.update(task.copy())
                } else {
                    app.tasks.create(task.copy())
                }
                setResult(RESULT_OK)
                finish()
            } else {
                Snackbar.make(it,
                    getString(R.string.enter_task_title),
                    Snackbar.LENGTH_LONG).show()
            }
        }

        // Set click listener for choose image button
        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher,this)
        }

        // Register image picker callback
        registerImagePickerCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_task, menu)
        if(edit) menu.getItem(1).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                setResult(RESULT_CANCELED)
                finish()
            }
            R.id.item_delete -> {
                app.tasks.delete(task)
                setResult(99)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")

                            val image = result.data!!.data!!
                            contentResolver.takePersistableUriPermission(image,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            task.image = image
                            Picasso.get()
                                .load(task.image)
                                .into(binding.taskImage)
                            binding.chooseImage.setText(R.string.change_task_image)
                        }
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }



    private fun showCalendarPicker() {
        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()

        picker.addOnPositiveButtonClickListener { selection ->
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.time = Date(selection)
            val selectedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
            // Update the selected date in the TextView
            selectedDateTextView.text = selectedDate
        }

        picker.show(supportFragmentManager, picker.toString())
    }
}

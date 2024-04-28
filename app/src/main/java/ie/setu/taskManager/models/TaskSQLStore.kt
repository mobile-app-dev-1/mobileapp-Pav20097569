package ie.setu.taskManager.models

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.net.toUri
import com.github.ajalt.timberkt.Timber
import timber.log.Timber.i

// SQLite database constants
private const val DATABASE_NAME = "tasks.db"
private const val TABLE_NAME = "tasks"
private const val COLUMN_ID = "id"
private const val COLUMN_TITLE = "title"
private const val COLUMN_DESCRIPTION = "description"
private const val COLUMN_IMAGE = "image"
private const val COLUMN_DATE = "date"




class TaskSQLStore(private val context: Context) : TaskStore {

    private var database: SQLiteDatabase

    init {
        // Set up local database connection
        database = TaskDbHelper(context).writableDatabase
    }

    override fun findAll(): List<TaskManagerModel> {
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = database.rawQuery(query, null)

        val tasks = ArrayList<TaskManagerModel>()

        cursor.use {
            while (it.moveToNext()) {
                tasks.add(

                    TaskManagerModel(
                        id = cursor.getLong(0),
                        title = cursor.getString(1),
                        description = cursor.getString(2),
                        image = cursor.getString(3).toUri(),
                        date = cursor.getString(4),

                        )
                )
            }
        }

        i("tasksdb : findAll() - has ${tasks.size} records")
        return tasks
    }



    override fun create(task: TaskManagerModel) {

        // Update the database for persistence beyond app lifetime
        val values = ContentValues()

        // ID is being managed by the table via auto increment
        values.put(COLUMN_TITLE, task.title)
        values.put(COLUMN_DESCRIPTION, task.description)
        values.put(COLUMN_IMAGE, task.image.toString())
        values.put(COLUMN_DATE, task.date)


        // Do the insert and set the transaction as successful
        task.id = database.insert(TABLE_NAME, null, values)
    }

    override fun update(task: TaskManagerModel) {
        val values = ContentValues().apply {
            put(COLUMN_TITLE, task.title)
            put(COLUMN_DESCRIPTION, task.description)
            put(COLUMN_IMAGE, task.image.toString())
            put(COLUMN_DATE, task.date)
        }

    }

    override fun delete(task: TaskManagerModel) {
        try {
            val selection = "$COLUMN_ID = ?"
            val selectionArgs = arrayOf(task.id.toString())

            val count = database.delete(TABLE_NAME, selection, selectionArgs)
            if (count != 1) {
                i("Failed to delete task")
            }
        } catch (e: Exception) {
            Timber.e { "Error deleting task" }
        }
    }


    private class TaskDbHelper(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

        private val createTableSQL =
            "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_TITLE TEXT, $COLUMN_DESCRIPTION TEXT, $COLUMN_IMAGE TEXT, " +
                    "$COLUMN_DATE TEXT)"

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(createTableSQL)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            // Handle database schema upgrades if needed
        }
    }
}
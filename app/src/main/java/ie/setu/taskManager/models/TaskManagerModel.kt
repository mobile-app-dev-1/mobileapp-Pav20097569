package ie.setu.taskManager.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class TaskManagerModel(
    var id: Long = 0,
    var title: String = "",
    var description: String = "",
    var image: String = "",
    var date: String = "",
) : Parcelable


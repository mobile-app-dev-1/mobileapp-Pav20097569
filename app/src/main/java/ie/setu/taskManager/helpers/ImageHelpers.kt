package ie.setu.taskManager.helpers
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import ie.setu.taskManager.R



//fun showImagePicker(intentLauncher: ActivityResultLauncher<Intent>) {
//  var chooseFile = Intent(Intent.ACTION_OPEN_DOCUMENT)
// chooseFile.type = "image/*"
//chooseFile = Intent.createChooser(chooseFile, R.string.select_task_image.toString())
//intentLauncher.launch(chooseFile)
//}


fun showImagePicker(intentLauncher: ActivityResultLauncher<Intent>, context: Context) {
    var imagePickerTargetIntent = Intent()

    imagePickerTargetIntent.action = Intent.ACTION_OPEN_DOCUMENT
    imagePickerTargetIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
    imagePickerTargetIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    imagePickerTargetIntent.type = "image/*"
    imagePickerTargetIntent = Intent.createChooser(imagePickerTargetIntent, context.getString(R.string.select_task_image))
    intentLauncher.launch(imagePickerTargetIntent)
}
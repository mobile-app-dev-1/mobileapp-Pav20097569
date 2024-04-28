package ie.setu.taskManager.activities

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import ie.setu.taskManager.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        // Set switch state based on saved preference
        val isDarkModeEnabled = sharedPreferences.getBoolean("isDarkModeEnabled", false)
        binding.darkModeSwitch.isChecked = isDarkModeEnabled

        // Set listener for switch state change
        binding.darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Save the state to SharedPreferences
            sharedPreferences.edit().putBoolean("isDarkModeEnabled", isChecked).apply()

            // Update the app's dark mode state
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}

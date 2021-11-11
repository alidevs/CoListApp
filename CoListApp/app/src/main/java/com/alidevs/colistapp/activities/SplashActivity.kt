package com.alidevs.colistapp.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.alidevs.colistapp.databinding.ActivitySplashBinding
import android.util.Log

import android.widget.Toast
import com.alidevs.colistapp.utils.Globals

class SplashActivity : AppCompatActivity() {

	private lateinit var binding: ActivitySplashBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivitySplashBinding.inflate(layoutInflater)
		setContentView(binding.root)

		Globals.sharedPreferences = getSharedPreferences("CoList", Context.MODE_PRIVATE)

		// TODO: Remove this button
		binding.tempLogoutButton.setOnClickListener {
			Globals.sharedPreferences.edit().clear().apply()
			finish()
			startActivity(Intent(this, this::class.java))
		}

		if (Globals.sharedPreferences.getBoolean("first_timer", true)) {
			// TODO: Move the segue to login here
			Toast.makeText(this, "This is your first time using the app, welcome :)", Toast.LENGTH_SHORT).show()
			Log.d("Comments", "First time")

			Globals.sharedPreferences.edit().putBoolean("first_timer", false).apply()
		}

		Handler(Looper.getMainLooper()).postDelayed({
			var intent = Intent(this, HomeActivity::class.java)
			if (Globals.sharedPreferences.getString("Email", null) == null) {
				intent = Intent(this, LoginActivity::class.java)
			}
			Toast.makeText(this, "Welcome back, ${Globals.sharedPreferences.getString("Fullname", "N/A")}", Toast.LENGTH_SHORT).show()
			startActivity(intent)
			finish()
		}, 1500)

	}
}
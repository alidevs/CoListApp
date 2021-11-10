package com.alidevs.colistapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alidevs.colistapp.databinding.ActivityMainBinding
import com.alidevs.colistapp.utils.Globals

class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)

		setContentView(binding.root)

		binding.mainActivityTextView.text = Globals.sharedPreferences.getString("Fullname", "N/A")
	}
}
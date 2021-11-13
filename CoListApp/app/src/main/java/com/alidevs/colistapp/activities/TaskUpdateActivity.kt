package com.alidevs.colistapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alidevs.colistapp.databinding.ActivityTaskUpdateBinding

class TaskUpdateActivity : AppCompatActivity() {
	private lateinit var binding: ActivityTaskUpdateBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityTaskUpdateBinding.inflate(layoutInflater)

		setContentView(binding.root)
	}
}
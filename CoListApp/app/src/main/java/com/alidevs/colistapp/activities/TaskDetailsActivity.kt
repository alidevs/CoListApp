package com.alidevs.colistapp.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.alidevs.colistapp.R
import com.alidevs.colistapp.databinding.ActivityTaskUpdateBinding
import com.alidevs.colistapp.models.ListModel
import com.alidevs.colistapp.models.TaskModel
import com.alidevs.colistapp.viewmodel.TaskViewModel
import com.google.gson.Gson

class TaskDetailsActivity : AppCompatActivity() {

	private lateinit var binding: ActivityTaskUpdateBinding
	private lateinit var task: TaskModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityTaskUpdateBinding.inflate(layoutInflater)

		setContentView(binding.root)
		setSupportActionBar(binding.materialToolbar)

		val jsonList = intent.getStringExtra("Task")
		task = Gson().fromJson(jsonList, TaskModel::class.java)

		with(binding) {
			materialToolbar.title = task.title
			materialToolbar.setTitleTextColor(Color.WHITE)

			listNameTextView.text = intent.getStringExtra("ListName")
			titleEditText.setText(task.title)
			descriptionEditText.setText(task.description)
			taskUpdateCreationDateTextView.text = "Created at ${task.createdAt}"
		}
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.task_details_menu, menu)
		return super.onCreateOptionsMenu(menu)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		val newTask = task
		newTask.title = binding.titleEditText.text.toString()
		newTask.description = binding.descriptionEditText.text.toString()
		val updateTask = TaskViewModel.updateTask(newTask)
		updateTask.observe(this, {
			Toast.makeText(this, "Task has been updated successfully!", Toast.LENGTH_SHORT).show()
		})
		return super.onOptionsItemSelected(item)
	}
}
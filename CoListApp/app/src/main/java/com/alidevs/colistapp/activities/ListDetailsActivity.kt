package com.alidevs.colistapp.activities

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.alidevs.colistapp.adapters.TaskAdapter
import com.alidevs.colistapp.databinding.ActivityListDetailsBinding
import com.alidevs.colistapp.databinding.CreateTaskDialogBinding
import com.alidevs.colistapp.models.ListModel
import com.alidevs.colistapp.models.TaskModel
import com.alidevs.colistapp.viewmodel.TaskViewModel
import com.google.gson.Gson

class ListDetailsActivity : AppCompatActivity() {

	private lateinit var binding: ActivityListDetailsBinding
	private lateinit var taskAdapter: TaskAdapter
	private lateinit var list: ListModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityListDetailsBinding.inflate(layoutInflater)

		setContentView(binding.root)

		val jsonList = intent.getStringExtra("List")
		list = Gson().fromJson(jsonList, ListModel::class.java)
		taskAdapter = TaskAdapter(list)

		with(binding) {
			listMaterialToolbar.title = list.name
			listMaterialToolbar.setTitleTextColor(Color.WHITE)

			taskRecyclerView.layoutManager = LinearLayoutManager(this@ListDetailsActivity, LinearLayoutManager.VERTICAL, false)
			taskRecyclerView.adapter = taskAdapter

			createTaskFAB.setOnClickListener {
				val dialogBinding = CreateTaskDialogBinding.inflate(LayoutInflater.from(this@ListDetailsActivity))
				val createTaskDialog = AlertDialog
					.Builder(this@ListDetailsActivity, 0)
					.create()

				with(dialogBinding) {
					createTaskDialogListNameTextView.text = list.name
					dialogCreateTaskButton.setOnClickListener {
						val taskName = createTaskDialogTaskNameEditText.text.toString()
						val taskDescription = createTaskDialogDescriptionEditText.text.toString()
						val taskModel = TaskModel(taskName, taskDescription, createTaskDialogCheckBox.isChecked, list._id!!)

						val createTask = TaskViewModel.createTask(taskModel)
						createTask.observe(this@ListDetailsActivity, {
							list.tasks = list.tasks?.plus(taskModel)
							taskAdapter.notifyDataSetChanged()
							createTaskDialog.dismiss()
						})
					}
					createTaskDialog.setView(root)
					createTaskDialog.show()
				}
			}
		}
	}
}
package com.alidevs.colistapp.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.ColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alidevs.colistapp.R
import com.alidevs.colistapp.adapters.TaskAdapter
import com.alidevs.colistapp.databinding.ActivityListDetailsBinding
import com.alidevs.colistapp.databinding.CreateTaskDialogBinding
import com.alidevs.colistapp.models.ListModel
import com.alidevs.colistapp.models.TaskModel
import com.alidevs.colistapp.utils.SwipeToDeleteCallback
import com.alidevs.colistapp.viewmodel.TaskViewModel
import com.google.gson.Gson
import java.util.*

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

			taskRecyclerView.layoutManager =
				LinearLayoutManager(this@ListDetailsActivity, LinearLayoutManager.VERTICAL, false)
			taskRecyclerView.adapter = taskAdapter
			toggleEmptyState()

			createTaskFAB.setOnClickListener {
				val createTaskDialogBinding =
					CreateTaskDialogBinding.inflate(LayoutInflater.from(this@ListDetailsActivity))
				val createTaskDialog = AlertDialog
					.Builder(this@ListDetailsActivity, 0)
					.create()
				val newTask = TaskModel("",
					"",
					false,
					list._id!!)

				createTaskDialog.window?.setBackgroundDrawable(ContextCompat.getDrawable(this@ListDetailsActivity,
					R.drawable.task_info))

				with(createTaskDialogBinding) {
					// Variables
					val calendar = Calendar.getInstance()
					val brightMaroonColor = ContextCompat.getColor(this@ListDetailsActivity,
						R.color.bright_maroon)

					createTaskDialogListNameTextView.text = list.name

					// Calendar button pressed
					createTaskDialogCalendarButton.setOnClickListener {
						val year = calendar.get(Calendar.YEAR)
						val month = calendar.get(Calendar.MONTH)
						val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

						val datePickerDialog =
							DatePickerDialog(this@ListDetailsActivity, { view, y, m, d ->
								newTask.dueDate = "$y/${m + 1}/$d"
								createTaskDialogBinding.createTaskDialogCalendarTextView.setTextColor(brightMaroonColor)
								createTaskDialogBinding.createTaskDialogCalendarTextView.text =
									newTask.dueDate.toString()
								Toast.makeText(this@ListDetailsActivity,
									newTask.dueDate.toString(),
									Toast.LENGTH_SHORT).show()
							}, year, month, dayOfMonth)
						datePickerDialog.setOnCancelListener {
							newTask.dueDate = ""
							createTaskDialogBinding.createTaskDialogCalendarTextView.text = ""
						}

						datePickerDialog.show()
					}

					// Time button pressed
					createTaskDialogTimeButton.setOnClickListener {
						val hour = calendar.get(Calendar.HOUR_OF_DAY)
						val minutes = calendar.get(Calendar.MINUTE)

						val timePickerDialog = TimePickerDialog(this@ListDetailsActivity,
							{ view, h, m ->
								newTask.reminder = "$h:$m"
								createTaskDialogBinding.createTaskDialogTimeTextView.text =
									newTask.reminder.toString()
								createTaskDialogBinding.createTaskDialogTimeTextView.setTextColor(
									brightMaroonColor)
								Toast.makeText(this@ListDetailsActivity,
									"Time set to ${newTask.reminder}",
									Toast.LENGTH_SHORT).show()
							}, hour, minutes, false)

						timePickerDialog.show()
					}

					// Create task button pressed
					dialogCreateTaskButton.setOnClickListener {
						newTask.title = createTaskDialogTaskNameEditText.text.toString()
						newTask.description = createTaskDialogDescriptionEditText.text.toString()

						val createTask = TaskViewModel.createTask(newTask)
						createTask.observe(this@ListDetailsActivity, {
							list.tasks?.add(newTask)
							toggleEmptyState()
							taskAdapter.notifyDataSetChanged()
							createTaskDialog.dismiss()
						})
					}

					createTaskDialog.setView(root)
					createTaskDialog.show()
				}
			}
		}

		val swipeHandler = object : SwipeToDeleteCallback(this) {
			override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
				val pos = viewHolder.adapterPosition
				val deletedTask = list.tasks?.get(pos)!!
				val deleteTask = TaskViewModel.deleteTask(deletedTask)
				deleteTask.observe(this@ListDetailsActivity, {
					Toast.makeText(this@ListDetailsActivity, "Task ${it.title} has been removed", Toast.LENGTH_SHORT).show()
					list.tasks!!.remove(deletedTask)
					taskAdapter.notifyDataSetChanged()
				})
			}
		}

		val itemTouchHelper = ItemTouchHelper(swipeHandler)
		itemTouchHelper.attachToRecyclerView(binding.taskRecyclerView)
	}


	private fun toggleEmptyState() {
		// Show & hide views
		with(binding) {
			if (list.tasks!!.size > 0) {
				emptyStateLayout.visibility = View.INVISIBLE
				taskRecyclerView.visibility = View.VISIBLE
			} else {
				emptyStateLayout.visibility = View.VISIBLE
				taskRecyclerView.visibility = View.INVISIBLE
			}
		}
	}
}
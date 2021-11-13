package com.alidevs.colistapp.adapters

import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.alidevs.colistapp.databinding.TaskRecyclerRowBinding
import com.alidevs.colistapp.models.ListModel
import com.alidevs.colistapp.models.TaskModel
import com.alidevs.colistapp.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class TaskAdapter(private var data: ListModel) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

	private lateinit var binding: TaskRecyclerRowBinding

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		binding = TaskRecyclerRowBinding.inflate(inflater, parent, false)

		return ViewHolder(binding.root)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		data.tasks?.get(position)?.let { holder.bind(it) }
	}

	override fun getItemCount(): Int = data.tasks!!.size

	inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		fun bind(taskModel: TaskModel) {
			with(binding) {
				taskRowCheckbox.text = taskModel.title
				taskRowCheckbox.isChecked = taskModel.completed

				taskRowDescriptionIndicatorImageView.visibility = if (taskModel.description!!.isEmpty()) View.GONE else View.VISIBLE
				if (taskModel.dueDate == null) {
					taskRowDateTextView.visibility = View.GONE
				} else {
					taskRowDateTextView.visibility = View.VISIBLE
					taskRowDateTextView.text = taskModel.dueDate.toString()
				}

				taskRowCheckbox.setOnClickListener {
					taskModel.completed = !taskModel.completed
					val updateTask = TaskViewModel.updateTask(taskModel)
					updateTask.observeForever {
						Toast.makeText(itemView.rootView.context, "Updated task ${it.title}", Toast.LENGTH_SHORT).show()
					}
				}
			}
		}
	}
}
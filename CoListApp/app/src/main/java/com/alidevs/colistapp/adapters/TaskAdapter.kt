package com.alidevs.colistapp.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.alidevs.colistapp.activities.ListDetailsActivity
import com.alidevs.colistapp.activities.TaskDetailsActivity
import com.alidevs.colistapp.databinding.TaskRecyclerRowBinding
import com.alidevs.colistapp.models.ListModel
import com.alidevs.colistapp.models.TaskModel
import com.alidevs.colistapp.viewmodel.TaskViewModel
import com.google.gson.Gson

class TaskAdapter(private var data: ListModel) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

	private lateinit var binding: TaskRecyclerRowBinding

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		binding = TaskRecyclerRowBinding.inflate(inflater, parent, false)

		return ViewHolder(binding.root)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val task = data.tasks?.get(position)
		task?.let { holder.bind(it) }

		holder.itemView.setOnClickListener {
			val gson = Gson()
			val intent = Intent(holder.itemView.context, TaskDetailsActivity::class.java)
			val jsonList = gson.toJson(task)
			intent.putExtra("Task", jsonList)
			intent.putExtra("ListName", data.name)
			holder.itemView.context.startActivity(intent)
		}
	}

	override fun getItemCount(): Int = data.tasks!!.size

	inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		fun bind(taskModel: TaskModel) {
			with(binding) {
				taskRowCheckbox.text = taskModel.title
				taskRowCheckbox.isChecked = taskModel.completed

				taskRowDescriptionIndicatorImageView.visibility =
					if (taskModel.description!!.isEmpty()) View.GONE else View.VISIBLE
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
						Toast.makeText(itemView.rootView.context,
							"Updated task ${it.title}",
							Toast.LENGTH_SHORT).show()
					}
				}
			}
		}
	}
}
package com.alidevs.colistapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alidevs.colistapp.databinding.TaskRecyclerRowBinding
import com.alidevs.colistapp.models.ListModel
import com.alidevs.colistapp.models.TaskModel

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
				checkBox.text = taskModel.title
				checkBox.isChecked = taskModel.completed
			}
		}
	}
}
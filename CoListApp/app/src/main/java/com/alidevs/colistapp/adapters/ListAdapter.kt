package com.alidevs.colistapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.alidevs.colistapp.databinding.HomeRecyclerRowBinding
import com.alidevs.colistapp.models.ListModel

class ListAdapter(private var data: MutableList<ListModel>) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

	private lateinit var binding: HomeRecyclerRowBinding

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		binding = HomeRecyclerRowBinding.inflate(inflater, parent, false)
		return ViewHolder(binding.root)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

	override fun getItemCount() = data.size

	inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		fun bind(listModel: ListModel) {
			with(binding) {
				listRowNameTextView.text = listModel.name
				listRowCountTextView.text = listModel.tasks?.count().toString()
				listRowIconImageView.setImageDrawable(ContextCompat.getDrawable(itemView.context,
					listModel.icon))

				Log.d("TasksList", listModel.tasks.toString())
			}
		}
	}

	// Pull to refresh
	fun clear() {
		data.clear()
		notifyDataSetChanged()
	}

	fun addAll(newData: List<ListModel>) {
		data.addAll(newData)
		notifyDataSetChanged()
	}
}
package com.alidevs.colistapp.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.alidevs.colistapp.R
import com.alidevs.colistapp.activities.ListDetailsActivity
import com.alidevs.colistapp.databinding.HomeRecyclerRowBinding
import com.alidevs.colistapp.models.ListModel
import com.google.gson.Gson

class ListAdapter(private var data: List<ListModel>) :
	RecyclerView.Adapter<ListAdapter.ViewHolder>() {

	private lateinit var binding: HomeRecyclerRowBinding

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		binding = HomeRecyclerRowBinding.inflate(inflater, parent, false)
		if (viewType == ViewType.SEPARATOR.ordinal) {
			return ViewHolder(LayoutInflater.from(parent.context)
				.inflate(R.layout.row_separator, parent, false))
		}

		return ViewHolder(binding.root)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val list = data[position]
		holder.bind(list)

		holder.itemView.setOnClickListener {
			val gson = Gson()
			val intent = Intent(holder.itemView.context, ListDetailsActivity::class.java)
			val jsonList = gson.toJson(list)
			intent.putExtra("List", jsonList)
			holder.itemView.context.startActivity(intent)
		}
	}

	override fun getItemViewType(position: Int): Int {
		return when (position) {
			3 -> ViewType.SEPARATOR.ordinal
			else -> ViewType.ROW.ordinal
		}
	}

	override fun getItemCount() = data.size

	inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		fun bind(listModel: ListModel) {
			with(binding) {
				listRowNameTextView.text = listModel.name
				listRowCountTextView.text = listModel.tasks?.count().toString()
				listRowIconImageView.setImageDrawable(ContextCompat.getDrawable(root.context,
					listModel.icon))
				Log.d("ListAdapter drawable", R.drawable.ic_list_today_icon.toString())
			}
		}
	}

	enum class ViewType {
		SEPARATOR, ROW
	}
}
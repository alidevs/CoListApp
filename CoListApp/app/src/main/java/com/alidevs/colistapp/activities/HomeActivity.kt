package com.alidevs.colistapp.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.alidevs.colistapp.R
import com.alidevs.colistapp.adapters.ListAdapter
import com.alidevs.colistapp.databinding.ActivityHomeBinding
import com.alidevs.colistapp.databinding.CreateListDialogBinding
import com.alidevs.colistapp.models.ListModel
import com.alidevs.colistapp.viewmodel.ListViewModel


class HomeActivity : AppCompatActivity() {

	private lateinit var binding: ActivityHomeBinding
	private lateinit var listAdapter: ListAdapter
	private val retrievedLists = mutableListOf<ListModel>()
	private val displayedList = mutableListOf<ListModel>()
	private val fixedLists = mutableListOf(
		ListModel(null, "Inbox", R.drawable.ic_list_inbox_icon, mutableListOf()),
		ListModel(null, "Today", R.drawable.ic_list_today_icon, mutableListOf()),
		ListModel(null, "Shared Lists", R.drawable.ic_list_shared_icon, mutableListOf()),
	)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityHomeBinding.inflate(layoutInflater)

		setContentView(binding.root)

		displayedList.addAll(fixedLists)
		displayedList.addAll(retrievedLists)
		listAdapter = ListAdapter(displayedList)
		binding.homeRecyclerView.layoutManager =
			LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
		binding.homeRecyclerView.adapter = listAdapter

		fetchData()

		// Pull to refresh
		binding.homeSwipeContainer.setOnRefreshListener {
			retrievedLists.clear()
			fetchData()
		}
	}

	fun createNewListButtonPressed(view: View) {

		val dialogBinding = CreateListDialogBinding.inflate(LayoutInflater.from(this))
		val createListDialog = AlertDialog
			.Builder(this, 0)
			.create()

		createListDialog.setView(dialogBinding.root)
		createListDialog.show()

	}

	private fun fetchData() {
		val userLists = ListViewModel.getLists()
		userLists.observe(this, {
			retrievedLists.addAll(it)
			updateLists()
			listAdapter.notifyDataSetChanged()
			binding.homeSwipeContainer.isRefreshing = false
		})
	}

	private fun updateLists() {
		Log.d("HomeLists", "Updating lists ..")
		displayedList.clear()
		displayedList.addAll(fixedLists)
		displayedList.addAll(retrievedLists)
	}
}
package com.alidevs.colistapp.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.alidevs.colistapp.R
import com.alidevs.colistapp.adapters.ListAdapter
import com.alidevs.colistapp.databinding.ActivityHomeBinding
import com.alidevs.colistapp.databinding.CreateListDialogBinding
import com.alidevs.colistapp.models.ListModel
import com.alidevs.colistapp.models.TaskModel
import com.alidevs.colistapp.utils.Globals
import com.alidevs.colistapp.viewmodel.ListViewModel

class HomeActivity : AppCompatActivity() {

	private lateinit var binding: ActivityHomeBinding
	private lateinit var listAdapter: ListAdapter
	private val retrievedLists = mutableListOf<ListModel>()
	private val displayedList = mutableListOf<ListModel>()
	private val fixedLists = mutableListOf(
		ListModel(null, null, "Inbox", R.drawable.ic_list_inbox_icon, mutableListOf(), false),
		ListModel(null, null, "Today", R.drawable.ic_list_today_icon, mutableListOf(), false),
		ListModel(null,
			null,
			"Shared Lists",
			R.drawable.ic_list_shared_icon,
			mutableListOf(),
			false),
		ListModel(null, null, "", R.drawable.ic_list_shared_icon, null, false)  // Empty list to fix an issue where the separator takes a place instead of the first retrieved ListModel
	)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityHomeBinding.inflate(layoutInflater)

		setContentView(binding.root)

		Globals.sharedPreferences = getSharedPreferences("CoList", Context.MODE_PRIVATE)

		displayedList.addAll(fixedLists)
		listAdapter = ListAdapter(displayedList)
		binding.homeRecyclerView.layoutManager =
			LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
		binding.homeRecyclerView.adapter = listAdapter
		binding.materialToolbar.setTitleTextColor(Color.WHITE)

		// Pull to refresh
		binding.homeSwipeContainer.setOnRefreshListener { swipeRefreshListener() }
		binding.createListButton.setOnClickListener { createNewListButtonPressed() }
	}

	override fun onResume() {
		super.onResume()
		fetchData()
	}

	private fun swipeRefreshListener() {
		fetchData()
	}

	private fun createNewListButtonPressed() {
		val dialogBinding = CreateListDialogBinding.inflate(LayoutInflater.from(this))
		val createListDialog = AlertDialog
			.Builder(this, 0)
			.create()

		createListDialog.setView(dialogBinding.root)
		createListDialog.show()

		createListDialog.window?.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.dialog_shape))
		dialogBinding.dialogCreateListButton.setOnClickListener {
			val listName = dialogBinding.createListEditText.text.toString()
			val createList = ListViewModel.createList(listName, R.drawable.ic_list_inbox_icon)
			createList.observe(this, {
				retrievedLists.add(it)
				updateLists()
				createListDialog.dismiss()
			})
		}
	}

	private fun fetchData() {
		binding.homeProgressBar.visibility = View.VISIBLE
		val userLists = ListViewModel.getLists()
		userLists.observeForever {
			Log.d("UserLists update", "New data found")
			retrievedLists.clear()
			retrievedLists.addAll(it)
			updateLists()
		}
	}

	private fun updateLists() {
		val inboxList = fixedLists[0]

		// Clear lists
		displayedList.clear()
		inboxList.tasks!!.clear()

		// Sort & filter lists
		retrievedLists.sortByDescending { it.updatedAt }
		retrievedLists.forEach { list ->
			val incompleteTasks = list.tasks!!.filter { task -> !task.completed }
			inboxList.tasks!!.addAll(incompleteTasks)
		}

		// Merge lists
		displayedList.addAll(fixedLists)
		displayedList.addAll(retrievedLists)

		// Show & hide views
		with(binding) {
			homeProgressBar.visibility = View.INVISIBLE
			homeSwipeContainer.isRefreshing = false
			if (retrievedLists.size > 0) {
				emptyStateLayout.visibility = View.INVISIBLE
				homeRecyclerView.visibility = View.VISIBLE
			} else {
				emptyStateLayout.visibility = View.VISIBLE
				homeRecyclerView.visibility = View.INVISIBLE
			}
		}
		listAdapter.notifyDataSetChanged()
	}
}
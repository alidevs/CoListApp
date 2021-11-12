package com.alidevs.colistapp.activities

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alidevs.colistapp.databinding.ActivityListDetailsBinding
import com.alidevs.colistapp.models.ListModel
import com.google.gson.Gson

class ListDetailsActivity : AppCompatActivity() {

	private lateinit var binding: ActivityListDetailsBinding
	private lateinit var list: ListModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityListDetailsBinding.inflate(layoutInflater)

		setContentView(binding.root)

		val jsonList = intent.getStringExtra("List")
		list = Gson().fromJson(jsonList, ListModel::class.java)

		with(binding) {
			listMaterialToolbar.title = list.name
			listMaterialToolbar.setTitleTextColor(Color.WHITE)
		}
	}
}
package com.alidevs.colistapp.models

import com.google.gson.annotations.SerializedName

data class ListModel(
	@SerializedName("_id")
	var _id: String? = null,

	@SerializedName("name")
	var name: String,

	@SerializedName("icon")
	var icon: Int,

	@SerializedName("tasks")
	var tasks: MutableList<TaskModel>?
)


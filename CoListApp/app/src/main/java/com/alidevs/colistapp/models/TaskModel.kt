package com.alidevs.colistapp.models

import com.google.gson.annotations.SerializedName

data class TaskModel(
	@SerializedName("_id")
	var _id: String,

	@SerializedName("title")
	var title: String,

	@SerializedName("description")
	var description: String,

	@SerializedName("completed")
	var completed: Boolean,

	@SerializedName("createdAt")
	var createdAt: String,

	@SerializedName("dueDate")
	var dueDate: Any,

	@SerializedName("geoLocation")
	var geoLocation: String,

	@SerializedName("reminder")
	var reminder: Any,

	@SerializedName("list")
	var listId: String
)
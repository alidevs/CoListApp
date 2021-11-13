package com.alidevs.colistapp.models

import com.google.gson.annotations.SerializedName

data class ListModel(
	@SerializedName("_id")
	var _id: String? = null,

	@SerializedName("_creator")
	var _creator: String? = null,

	@SerializedName("name")
	var name: String,

	@SerializedName("icon")
	var icon: Int,

	@SerializedName("tasks")
	var tasks: MutableList<TaskModel>?,

	@SerializedName("shared")
	var shared: Boolean,

	@SerializedName("createdAt")
	var createdAt: String? = null,

	@SerializedName("updatedAt")
	var updatedAt: String? = null
)
package com.alidevs.colistapp.models

import com.google.gson.annotations.SerializedName

data class User(
	@SerializedName("_id")
	var id: String?,

	@SerializedName("fullname")
	var fullname: String?,

	@SerializedName("email")
	var email: String?,

	var password: String
) {
	constructor(email: String?, password: String) : this(null, null, email, password)
	constructor(fullname: String?, email: String?, password: String) : this(null, fullname, email, password)
}
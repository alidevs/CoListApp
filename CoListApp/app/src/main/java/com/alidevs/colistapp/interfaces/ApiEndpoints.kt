package com.alidevs.colistapp.interfaces

import com.alidevs.colistapp.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiEndpoints {
	companion object {
		const val BASE_URL = "http://10.0.2.2:3000"
	}

	@POST("/users/register")
	fun userRegister(@Body user: User) : Call<User>

	@POST("/users/login")
	fun userLogin(@Body user: User) : Call<User>

	@GET("/users/me")
	fun userMe() : Call<User>
}
package com.alidevs.colistapp.api

import com.alidevs.colistapp.models.ListModel
import com.alidevs.colistapp.models.UserModel
import com.alidevs.colistapp.utils.Globals
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiEndpoints {
	companion object {
		const val BASE_URL = "http://10.0.2.2:3000"

		var retrofitService: ApiEndpoints? = null

		fun getInstance() : ApiEndpoints {

			if (retrofitService == null) {
				val retrofit = Retrofit.Builder()
					.baseUrl(BASE_URL)
					.addConverterFactory(GsonConverterFactory.create())
					.build()
				retrofitService = retrofit.create(ApiEndpoints::class.java)
			}
			return retrofitService!!
		}
	}

	// User authentication routes
	@POST("/users/register")
	fun userRegister(@Body user: UserModel) : Call<UserModel>

	@POST("/users/login")
	fun userLogin(@Body user: UserModel) : Call<UserModel>

	@GET("/users/me")
	fun userMe() : Call<UserModel>

	// List interaction routes
	@GET("/lists")
	fun getUserLists(@Header("x-auth") xAuthHeader: String) : Call<List<ListModel>>

}
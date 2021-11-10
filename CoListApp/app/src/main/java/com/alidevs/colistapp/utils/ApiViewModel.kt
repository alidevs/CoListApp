package com.alidevs.colistapp.utils

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alidevs.colistapp.interfaces.ApiEndpoints
import com.alidevs.colistapp.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiViewModel : ViewModel() {
	val apiEndpoints: ApiEndpoints

	companion object {
		var instance: ApiViewModel? = ApiViewModel()

		fun userRegister(user: User): LiveData<User> {
			Log.d("ApiClient", user.toString())
			return handleUserAuthentication(user, ApiCallType.Register)
		}

		fun userLogin(user: User): LiveData<User> {
			return handleUserAuthentication(user, ApiCallType.Login)
		}

		private fun handleUserAuthentication(
			user: User,
			apiCallType: ApiCallType,
		): MutableLiveData<User> {
			val liveData = MutableLiveData<User>()
			val apiEndpoints = instance!!.apiEndpoints

			val call = when (apiCallType) {
				ApiCallType.Login -> apiEndpoints.userLogin(user)
				else -> apiEndpoints.userRegister(user)
			}

			call.enqueue(object : Callback<User> {
				override fun onResponse(call: Call<User>, response: Response<User>) {
					if (response.isSuccessful) {
						Log.d("Retrofit response body", response.body().toString())
						saveUserInfo(response)
						liveData.value = response.body()
					} else {
						Log.d("Retrofit", response.code().toString())
					}
				}

				override fun onFailure(call: Call<User>, t: Throwable) {
					t.message?.let { Log.d("Retrofit error", it) }
					t.printStackTrace()
				}
			})

			return liveData
		}

		fun saveUserInfo(response: Response<User>) {
			val editor = Globals.sharedPreferences.edit()
			val token = response.headers().get("x-auth")
			val name = response.body()!!.fullname
			val email = response.body()!!.email

			editor.putString("Token", token)
			editor.putString("Fullname", name)
			editor.putString("Email", email)

			if (!editor.commit()) {
				Log.d("Retrofit editor", "Failed to commit edits")
				return
			}
		}
	}

	init {
		val retrofit = Retrofit.Builder()
			.baseUrl(ApiEndpoints.BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.build()
		apiEndpoints = retrofit.create(ApiEndpoints::class.java)
	}
}

enum class ApiCallType {
	Login, Register, Me
}
package com.alidevs.colistapp.utils

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alidevs.colistapp.api.ApiEndpoints
import com.alidevs.colistapp.models.UserModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AuthenticationViewModel : ViewModel() {

	companion object {
		fun userRegister(user: UserModel) =
			handleUserAuthentication(user, ApiCallType.Register)

		fun userLogin(user: UserModel) =
			handleUserAuthentication(user, ApiCallType.Login)

		private fun handleUserAuthentication(
			user: UserModel,
			apiCallType: ApiCallType,
		): MutableLiveData<UserModel> {
			val liveData = MutableLiveData<UserModel>()
			val apiEndpoints = ApiEndpoints.getInstance()

			val call = when (apiCallType) {
				ApiCallType.Login -> apiEndpoints.userLogin(user)
				else -> apiEndpoints.userRegister(user)
			}

			call.enqueue(object : Callback<UserModel> {
				override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
					if (response.isSuccessful) {
						Log.d("Retrofit response body", response.body().toString())
						saveUserInfo(response)
						liveData.value = response.body()
					} else {
						Log.d("Retrofit", response.code().toString())
					}
				}

				override fun onFailure(call: Call<UserModel>, t: Throwable) {
					t.message?.let { Log.d("Retrofit error", it) }
					t.printStackTrace()
				}
			})

			return liveData
		}

		fun saveUserInfo(response: Response<UserModel>) {
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

}

enum class ApiCallType {
	Login, Register, Me
}
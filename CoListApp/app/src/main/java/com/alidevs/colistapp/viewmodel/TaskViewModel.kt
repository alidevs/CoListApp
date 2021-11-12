package com.alidevs.colistapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alidevs.colistapp.api.ApiEndpoints
import com.alidevs.colistapp.models.ListModel
import com.alidevs.colistapp.models.TaskModel
import com.alidevs.colistapp.utils.Globals
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskViewModel: ViewModel() {

	companion object {
		val apiEndpoints = ApiEndpoints.getInstance()
		val xAuthHeader = Globals.sharedPreferences.getString("Token", "")!!

		fun createTask(task: TaskModel): MutableLiveData<ListModel> {
			val url = ApiEndpoints.BASE_URL + "/lists/${task.listId}"
			val liveData = MutableLiveData<ListModel>()
			val call = apiEndpoints.createTask(url, xAuthHeader, task)

			call.enqueue(object : Callback<ListModel> {
				override fun onResponse(call: Call<ListModel>, response: Response<ListModel>) {
					if (response.isSuccessful) {
						liveData.value = response.body()
					} else {
						Log.d("Retrofit", response.code().toString())
					}
				}

				override fun onFailure(call: Call<ListModel>, t: Throwable) {
					t.message?.let { Log.d("Retrofit", it) }
				}

			})

			return liveData
		}
	}
}
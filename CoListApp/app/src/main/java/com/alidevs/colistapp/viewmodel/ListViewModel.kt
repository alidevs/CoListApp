package com.alidevs.colistapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alidevs.colistapp.api.ApiEndpoints
import com.alidevs.colistapp.models.ListModel
import com.alidevs.colistapp.utils.Globals
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListViewModel: ViewModel() {

	companion object {
		val apiEndpoints = ApiEndpoints.getInstance()
		val xAuthHeader = Globals.sharedPreferences.getString("Token", "")!!

		fun getLists(): MutableLiveData<List<ListModel>> {
			val liveData = MutableLiveData<List<ListModel>>()
			val call = apiEndpoints.getLists(xAuthHeader)

			call.enqueue(object : Callback<List<ListModel>> {
				override fun onResponse(call: Call<List<ListModel>>, response: Response<List<ListModel>>) {
					if (response.isSuccessful) {
						Log.d("ListViewModel", response.body().toString())
						liveData.postValue(response.body())
					} else {
						Log.d("Retrofit", response.code().toString())
					}
				}

				override fun onFailure(call: Call<List<ListModel>>, t: Throwable) {
					t.message?.let { Log.d("Retrofit error", it) }
					t.printStackTrace()
				}
			})
			return liveData
		}

		fun createList(listName: String, listIcon: Int) : MutableLiveData<ListModel> {
			val liveData = MutableLiveData<ListModel>()
			val listModel = ListModel(null, null, listName, listIcon, null, false)
			val call = apiEndpoints.createList(xAuthHeader, listModel)

			call.enqueue(object : Callback<ListModel> {
				override fun onResponse(call: Call<ListModel>, response: Response<ListModel>) {
					if (response.isSuccessful) {
						liveData.postValue(response.body())
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
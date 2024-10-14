package com.example.dicodingevent.ui.upcoming

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.response.EventResponse
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingViewModel : ViewModel() {

    private val _eventList = MutableLiveData<List<ListEventsItem?>?>()
    val eventList: LiveData<List<ListEventsItem?>?> = _eventList

    private val _isLoadingData = MutableLiveData<Boolean>()
    val isLoadingData: LiveData<Boolean> = _isLoadingData

    private val _isDataLoadedSuccessfully = MutableLiveData<Boolean>()
    val isDataLoadedSuccessfully: LiveData<Boolean> = _isDataLoadedSuccessfully

    companion object {
        private const val LOG_TAG = "UpcomingEventViewModel"
    }

    init {
        loadEvents()
    }

    private fun loadEvents() {
        _isLoadingData.value = true
        val apiClient = ApiConfig.getApiService().getEvents(1)
        apiClient.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoadingData.value = false
                if (response.isSuccessful) {
                    _eventList.value = response.body()?.listEvents
                    _isDataLoadedSuccessfully.value = true
                } else {
                    Log.e(LOG_TAG, "Gagal memuat data: ${response.message()}")
                    _isDataLoadedSuccessfully.value = false
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoadingData.value = false
                _isDataLoadedSuccessfully.value = false
                Log.e(LOG_TAG, "Gagal memuat data: ${t.message}")
            }
        })
    }
}
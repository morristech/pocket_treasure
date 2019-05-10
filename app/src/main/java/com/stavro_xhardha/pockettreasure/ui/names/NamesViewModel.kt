package com.stavro_xhardha.pockettreasure.ui.names

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stavro_xhardha.pockettreasure.brain.STATIC_CODE_OK
import com.stavro_xhardha.pockettreasure.model.Name
import kotlinx.coroutines.*
import java.net.UnknownHostException
import javax.inject.Inject

class NamesViewModel @Inject constructor(private val repository: NamesRepository) : ViewModel() {

    private val completableJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + completableJob)

    var allNamesList: MutableLiveData<ArrayList<Name>> = MutableLiveData()
    var progressBarVisibility: MutableLiveData<Int> = MutableLiveData()
    var errorLayoutVisibility: MutableLiveData<Int> = MutableLiveData()

    init {
        loadNamesList()
    }

    private fun loadNamesList() {
        coroutineScope.launch {
            withContext(Dispatchers.Main) {
                progressBarVisibility.value = View.VISIBLE
            }
            try {
                val namesResponse = repository.fetchNintyNineNamesAsync().await()
                withContext(Dispatchers.Main) {
                    if (namesResponse.code == STATIC_CODE_OK) {
                        allNamesList.value = namesResponse.data
                        progressBarVisibility.value = View.GONE
                    } else {
                        errorLayoutVisibility.value = View.VISIBLE
                        progressBarVisibility.value = View.GONE
                    }
                }
            } catch (e: UnknownHostException) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    errorLayoutVisibility.value = View.VISIBLE
                    progressBarVisibility.value = View.GONE
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        completableJob.cancel()
    }
}
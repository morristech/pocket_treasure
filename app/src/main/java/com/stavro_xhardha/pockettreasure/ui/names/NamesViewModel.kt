package com.stavro_xhardha.pockettreasure.ui.names

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stavro_xhardha.pockettreasure.brain.STATIC_CODE_OK
import com.stavro_xhardha.pockettreasure.model.CoroutineDispatcher
import com.stavro_xhardha.pockettreasure.model.Name
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import javax.inject.Inject

class NamesViewModel @Inject constructor(
    private val repository: NamesRepository,
    private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val completableJob = Job()
    private val networkScope = CoroutineScope(coroutineDispatcher.network + completableJob)

    var allNamesList: MutableLiveData<ArrayList<Name>> = MutableLiveData()
    var progressBarVisibility: MutableLiveData<Int> = MutableLiveData()
    var errorLayoutVisibility: MutableLiveData<Int> = MutableLiveData()

    init {
        loadNamesList()
    }

    private fun loadNamesList() {
        networkScope.launch {
            withContext(coroutineDispatcher.mainThread) {
                progressBarVisibility.value = View.VISIBLE
            }
            try {
                val namesResponse = repository.fetchNintyNineNamesAsync().await()
                withContext(coroutineDispatcher.mainThread) {
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
                withContext(coroutineDispatcher.mainThread) {
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
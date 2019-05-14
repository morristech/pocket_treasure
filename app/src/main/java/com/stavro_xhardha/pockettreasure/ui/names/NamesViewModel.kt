package com.stavro_xhardha.pockettreasure.ui.names

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stavro_xhardha.pockettreasure.brain.STATIC_CODE_OK
import com.stavro_xhardha.pockettreasure.model.Name
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import javax.inject.Inject

class NamesViewModel @Inject constructor(private val repository: NamesRepository) : ViewModel() {

    var allNamesList: MutableLiveData<ArrayList<Name>> = MutableLiveData()
    var progressBarVisibility: MutableLiveData<Int> = MutableLiveData()
    var errorLayoutVisibility: MutableLiveData<Int> = MutableLiveData()

    init {
        loadNamesList()
    }

    private fun loadNamesList() {
        viewModelScope.launch(Dispatchers.IO) {
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
}
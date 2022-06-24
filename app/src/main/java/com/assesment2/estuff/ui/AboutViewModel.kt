package com.assesment2.estuff.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assesment2.estuff.model.AboutApp
import com.assesment2.estuff.network.LoadingIndicator
import com.assesment2.estuff.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AboutViewModel : ViewModel() {

    private val _copyRightText = MutableLiveData<AboutApp>()
    private val _status = MutableLiveData<LoadingIndicator>()

    init {
        getCopyRight()
    }

    private fun getCopyRight(){
        viewModelScope.launch (Dispatchers.IO){
            _status.postValue(LoadingIndicator.LOADING)
           try {
               _copyRightText.postValue(RetrofitInstance.service.getCopyRightText())
               _status.postValue(LoadingIndicator.SUCCESS)
           }
           catch (e:Exception){
               Log.d("AboutViewModel", "Failure: ${e.message}")
               _status.postValue(LoadingIndicator.FAILED)
           }
        }
    }

//val copyRightText get() =  _copyRightText
    fun getCopyRightText(): LiveData<AboutApp> = _copyRightText
//    val status get() =  _status
    fun getStatus(): LiveData<LoadingIndicator> = _status
}




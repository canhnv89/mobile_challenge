package com.example.mobilechallenge.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobilechallenge.model.CardInfo
import com.example.mobilechallenge.utils.formatter.TextFormatter

class MainViewModel : ViewModel() {
    private val cardInfoLiveData = MutableLiveData<CardInfo>()
    private val isCardValidLiveData = MutableLiveData<Boolean>().apply { postValue(true) }

    fun getCardInfo(): LiveData<CardInfo> {
        return cardInfoLiveData
    }

    fun updateCardInfo(cardNum: String, expiryDate: String, cvv: String) {
        val cardInfo = CardInfo(cardNum, expiryDate, cvv)
        cardInfoLiveData.postValue(cardInfo)
        isCardValidLiveData.postValue(cardInfo.isValid())
    }

    fun isCardValid(): LiveData<Boolean> {
        return isCardValidLiveData
    }


}
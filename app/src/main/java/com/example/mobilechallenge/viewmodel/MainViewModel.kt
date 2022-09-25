package com.example.mobilechallenge.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobilechallenge.R
import com.example.mobilechallenge.api.ApiResult
import com.example.mobilechallenge.model.CardInfo
import com.example.mobilechallenge.payment.PaymentHelper

class MainViewModel : ViewModel() {
    private var cardInfo: CardInfo? = null
    private val isCardValidLiveData = MutableLiveData<Boolean>().apply { postValue(true) }
    private val isUiBusyLiveData = MutableLiveData<Boolean>().apply { postValue(false) }

    companion object {
        private const val TAG = "MainViewModel"
    }

    fun getCardInfo(): CardInfo? {
        return cardInfo
    }

    fun updateCardInfo(cardNum: String, expiryDate: String, cvv: String) {
        cardInfo = CardInfo(cardNum, expiryDate, cvv)
        isCardValidLiveData.postValue(cardInfo?.isValid())
    }

    fun isCardValid(): LiveData<Boolean> {
        return isCardValidLiveData
    }

    fun isUiBusy(): LiveData<Boolean> {
        return isUiBusyLiveData
    }

    suspend fun submitPay(context: Context): ApiResult<String> {
        val submitCardInfo = cardInfo
        return if (submitCardInfo == null || !submitCardInfo.isValid()) {
            Log.e(TAG, "Card info is null or not valid")
            ApiResult.Error(context.getString(R.string.pay_error_message_card_invalid))
        } else {
            isUiBusyLiveData.postValue(true)
            val result = PaymentHelper.submitPay(submitCardInfo)
            Log.i(TAG, "response Url = $result")
            isUiBusyLiveData.postValue(false)
            result
        }
    }
}
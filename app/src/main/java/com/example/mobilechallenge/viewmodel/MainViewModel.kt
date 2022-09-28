package com.example.mobilechallenge.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobilechallenge.R
import com.example.mobilechallenge.api.ApiResult
import com.example.mobilechallenge.api.ApiStatus
import com.example.mobilechallenge.model.CardInfo
import com.example.mobilechallenge.model.CardType
import com.example.mobilechallenge.payment.PaymentHelper

class MainViewModel : ViewModel() {
    private var cardInfo: CardInfo? = null
    private val isCardValidLiveData = MutableLiveData<Boolean>().apply { postValue(true) }
    private val isUiBusyLiveData = MutableLiveData<Boolean>().apply { postValue(false) }
    private var payConfirmUrl: String? = null
    private val cardTypeLiveData =
        MutableLiveData<Int>().apply { value = R.drawable.ic_card_number}

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

    fun getPayConfirmUrl() = payConfirmUrl

    fun setCardType(cardType: CardType) {
        when (cardType) {
            CardType.VISA -> cardTypeLiveData.postValue(R.drawable.visa)
            CardType.MASTER -> cardTypeLiveData.postValue(R.drawable.master)
            CardType.AMEX -> cardTypeLiveData.postValue(R.drawable.amex)
            else -> cardTypeLiveData.postValue(R.drawable.ic_card_number)
        }
    }

    fun getCardIconLiveData(): LiveData<Int> = cardTypeLiveData

    suspend fun submitPay(context: Context): ApiResult<String> {
        val submitCardInfo = cardInfo
        return if (submitCardInfo == null || !submitCardInfo.isValid()) {
            Log.e(TAG, "Card info is null or not valid")
            ApiResult.Error(context.getString(R.string.pay_error_message_card_invalid))
        } else {
            isUiBusyLiveData.postValue(true)
            val result = PaymentHelper.submitPay(submitCardInfo)
            Log.i(TAG, "response Url = $result")
            payConfirmUrl = if (result.status == ApiStatus.SUCCESS && result.data != null) {
                result.data
            } else {
                null
            }
            isUiBusyLiveData.postValue(false)
            result
        }
    }
}
package com.example.mobilechallenge.payment

import com.example.mobilechallenge.api.ApiResult
import com.example.mobilechallenge.api.ServerApi
import com.example.mobilechallenge.model.CardInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PaymentHelper {
    companion object {
        private const val SUCCESS_URL = "https://success.com"
        private const val FAILURE_URL = "https://failure.com"
        private val api: ServerApi by lazy {
            ServerApi.create()
        }

        suspend fun submitPay(cardInfo: CardInfo): ApiResult<String> = withContext(Dispatchers.IO) {
            val payRequest = ServerApi.PayRequest(
                cardInfo.getExpiryMonth(),
                cardInfo.getExpiryYear(),
                cardInfo.cvv,
                cardInfo.cardNum,
                SUCCESS_URL,
                FAILURE_URL
            )
            val response = api.submitPay(payRequest)
            if (response.isSuccessful) {
                return@withContext ApiResult.Success(response.body()?.url)
            } else {
                return@withContext ApiResult.Error(response.message())
            }
        }
    }


}
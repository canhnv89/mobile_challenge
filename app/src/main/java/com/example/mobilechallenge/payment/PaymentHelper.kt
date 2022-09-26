package com.example.mobilechallenge.payment

import com.example.mobilechallenge.api.ApiResult
import com.example.mobilechallenge.api.ServerApi
import com.example.mobilechallenge.model.CardInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * A helper class to submit a payment request to ServerAPI.
 * This class will transform a CardInfo object into a PayRequest which defined
 * by in Restful API
 */
class PaymentHelper {
    companion object {
        const val SUCCESS_URL = "https://success.com"
        const val FAILURE_URL = "https://failure.com"
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
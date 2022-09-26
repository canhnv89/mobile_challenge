package com.example.mobilechallenge.api

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

/**
 * Interface of ServerAPI which is implemented by Retrofit
 */
interface ServerApi {

    @POST("/pay")
    suspend fun submitPay(
        @Body requestBody: PayRequest
    ): Response<PayResponse>

    class PayResponse(val url: String)

    data class PayRequest(
        val expiry_month: String,
        val expiry_year: String,
        val cvv: String,
        val number: String,
        val success_url: String,
        val failure_url: String
    )

    companion object {
        private const val BASE_URL = "https://integrations-cko.herokuapp.com/"
        private const val TAG = "ServerApi"
        fun create(): ServerApi {
            val logger = HttpLoggingInterceptor { Log.d(TAG, it) }
            logger.level = HttpLoggingInterceptor.Level.BASIC
            val client = OkHttpClient.Builder().addInterceptor(logger).build()
            return Retrofit.Builder().baseUrl(BASE_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(ServerApi::class.java)
        }
    }
}
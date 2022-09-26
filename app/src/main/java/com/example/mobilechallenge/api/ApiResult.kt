package com.example.mobilechallenge.api

enum class ApiStatus{
    SUCCESS,
    ERROR
}

/**
 * Class to return result of API calling
 * @param status ApiStatus.SUCCESS or ApiStatus.ERROR
 * @param data not NULL when status is ApiStatus.SUCCESS otherwise NULL
 * @param message not NULL when status is ApiStatus.ERROR otherwise NULL
 */
sealed class ApiResult <out T> (val status: ApiStatus, val data: T?, val message:String?) {

    data class Success<out R>(val _data: R?): ApiResult<R>(
        status = ApiStatus.SUCCESS,
        data = _data,
        message = null
    )

    data class Error(val exception: String): ApiResult<Nothing>(
        status = ApiStatus.ERROR,
        data = null,
        message = exception
    )
}

package com.github.crisacm.testskills.data.network

sealed class ApiResult<out R> {
    data class Success<out T>(val data: T): ApiResult<T>()
    data class Failure(val message: String): ApiResult<Nothing>()
    data class Error(val t: Throwable): ApiResult<Nothing>()
}

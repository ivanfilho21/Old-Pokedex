package br.iwan.oldpokedex.data.remote.model

sealed class ApiRequestResult {
    data class Success(val data: String) : ApiRequestResult()
    data class Error(val throwable: Throwable) : ApiRequestResult()
}
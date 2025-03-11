package br.iwan.oldpokedex.data.model

sealed class UiResponse<out T> {
    data object Loading : UiResponse<Nothing>()
    data class Success<R>(val data: R) : UiResponse<R>()
    data class Error(val message: String) : UiResponse<Nothing>()
}
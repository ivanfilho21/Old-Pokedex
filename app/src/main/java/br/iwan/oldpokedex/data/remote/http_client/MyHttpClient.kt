package br.iwan.oldpokedex.data.remote.http_client

import br.iwan.oldpokedex.data.remote.model.ApiRequestResult
import okhttp3.Request
import java.io.IOException

class MyHttpClient private constructor() {
    private var requestUrl = ""
    private var requestType = HttpRequestType.GET

    class Builder {
        private val client = MyHttpClient()

        fun setUrl(url: String): Builder {
            client.requestUrl = url
            return this
        }

        fun setRequestType(type: HttpRequestType): Builder {
            client.requestType = type
            return this
        }

        fun build(): MyHttpClient {
            return client
        }
    }

    fun sendRequest(): ApiRequestResult {
        val request = Request.Builder().apply {
            when (requestType) {
                HttpRequestType.GET -> get()
            }
            url(requestUrl)
        }.build()

        var errorMessage: String

        try {
            MyOkHttpClientProvider.client.newCall(request)
                .execute()
                .use { response ->
                    if (response.isSuccessful) {
                        response.body?.use { responseBody ->
                            return ApiRequestResult.Success(responseBody.string())
                        }
                    }

                    errorMessage = response.message
                }
        } catch (e: IOException) {
            errorMessage = e.message.orEmpty()
        }

        return getErrorObject(errorMessage)
    }

    private fun getErrorObject(message: String) = ApiRequestResult.Error(Throwable(message))
}
package br.iwan.oldpokedex.data.remote.http_client

import android.util.Log
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object MyOkHttpClientProvider {
    private const val TAG = "PokedexHttpClient"

    val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                chain.request()
                    .newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .build()
                    .let { request ->
                        chain.proceed(request)
                    }
            }
            .addInterceptor { chain ->
                val (url, method) = chain.request().let {
                    Pair(it.url, it.method)
                }

                chain.proceed(chain.request()).also { res ->
                    Log.d(
                        TAG,
                        "-----------------------------------------------" +
                                "\n   Http Request\n\tURL: $url" +
                                "\n   Method: $method" +
                                "\n   Response:" +
                                "\n   ${res.peekBody(Long.MAX_VALUE).string()}" +
                                "\n-----------------------------------------------"
                    )
                }
            }
            .retryOnConnectionFailure(true)
            .build()
    }
}
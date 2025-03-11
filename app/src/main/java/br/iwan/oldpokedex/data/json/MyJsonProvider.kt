package br.iwan.oldpokedex.data.json

import kotlinx.serialization.json.Json

object MyJsonProvider {
    val json by lazy {
        Json { ignoreUnknownKeys = true }
    }
}
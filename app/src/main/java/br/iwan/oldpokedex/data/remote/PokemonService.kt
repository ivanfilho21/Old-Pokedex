package br.iwan.oldpokedex.data.remote

import br.iwan.oldpokedex.data.remote.http_client.HttpRequestType
import br.iwan.oldpokedex.data.remote.http_client.MyHttpClient
import br.iwan.oldpokedex.data.remote.model.ApiRequestResult
import javax.inject.Inject

class PokemonService @Inject constructor() {
    fun getAllPokemon(limit: Int): ApiRequestResult {
        return MyHttpClient.Builder()
            .setUrl("https://pokeapi.co/api/v2/pokemon?limit=$limit")
            .setRequestType(HttpRequestType.GET)
            .build()
            .sendRequest()
    }
}
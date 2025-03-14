package br.iwan.oldpokedex.data.remote

import br.iwan.oldpokedex.data.remote.http_client.HttpRequestType
import br.iwan.oldpokedex.data.remote.http_client.MyHttpClient
import br.iwan.oldpokedex.data.remote.model.ApiRequestResult
import javax.inject.Inject

class PokemonService @Inject constructor() {
    companion object {
        private const val BASE_URL = "https://pokeapi.co/api/v2/pokemon"
        private const val GET_POKEMON_ENDPOINT = "$BASE_URL/%d"
        private const val GET_SPECIES_DETAILS_ENDPOINT = "$BASE_URL-species/%d"
        private const val GET_ENCOUNTERS_ENDPOINT = "$BASE_URL/%d/encounters"
    }

    fun getPokemonList(limit: Int): ApiRequestResult {
        return MyHttpClient.Builder()
            .setUrl("$BASE_URL?limit=$limit")
            .setRequestType(HttpRequestType.GET)
            .build()
            .sendRequest()
    }

    fun getPokemonDetails(id: Int): ApiRequestResult {
        return MyHttpClient.Builder()
            .setUrl(GET_POKEMON_ENDPOINT.format(id))
            .setRequestType(HttpRequestType.GET)
            .build()
            .sendRequest()
    }

    fun getSpeciesDetails(pokemonId: Int): ApiRequestResult {
        return MyHttpClient.Builder()
            .setUrl(GET_SPECIES_DETAILS_ENDPOINT.format(pokemonId))
            .setRequestType(HttpRequestType.GET)
            .build()
            .sendRequest()
    }

    fun getEncountersDetails(pokemonId: Int) =
        MyHttpClient.Builder()
            .setUrl(GET_ENCOUNTERS_ENDPOINT.format(pokemonId))
            .setRequestType(HttpRequestType.GET)
            .build()
            .sendRequest()
}
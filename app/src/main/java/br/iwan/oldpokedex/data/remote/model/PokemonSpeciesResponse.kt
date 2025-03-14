package br.iwan.oldpokedex.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object PokemonSpeciesResponse {
    @Serializable
    data class Data(
        @SerialName("flavor_text_entries") val descriptions: List<Description>? = null
    )

    @Serializable
    data class Description(
        @SerialName("flavor_text") val text: String? = null,
        val language: NameUrlObject? = null,
        val version: NameUrlObject? = null
    )
}
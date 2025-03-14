package br.iwan.oldpokedex.data.remote.model

import kotlinx.serialization.Serializable

object PokemonResponse {
    @Serializable
    data class Data(
        val name: String? = null,
        val url: String? = null,
        val height: Int? = null,
        val weight: Int? = null,
        val types: List<PokemonType>? = null
    )

    @Serializable
    data class PokemonType(val type: NameUrlObject? = null)
}
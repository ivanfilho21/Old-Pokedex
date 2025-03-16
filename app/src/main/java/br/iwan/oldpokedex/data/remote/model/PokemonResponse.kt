package br.iwan.oldpokedex.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object PokemonResponse {
    @Serializable
    data class Data(
        val name: String? = null,
        val url: String? = null,
        val height: Int? = null,
        val weight: Int? = null,
        val types: List<PokemonType>? = null,
        val stats: List<StatObject>? = null
    )

    @Serializable
    data class PokemonType(val type: NameUrlObject? = null)

    @Serializable
    data class StatObject(
        @SerialName("base_stat") val baseStat: Int? = null,
        val effort: Int? = null,
        val stat: NameUrlObject? = null
    )
}
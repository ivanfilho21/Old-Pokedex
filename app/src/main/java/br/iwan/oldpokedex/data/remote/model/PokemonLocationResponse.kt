package br.iwan.oldpokedex.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object PokemonLocationResponse {
    @Serializable
    data class Data(
        @SerialName("location_area") val locationArea: NameUrlObject? = null,
        @SerialName("version_details") val versions: List<Details>? = null
    )

    @Serializable
    data class Details(
        @SerialName("encounter_details") val encounters: List<Encounter>? = null,
        @SerialName("max_chance") val maxChance: Int? = null,
        val version: NameUrlObject? = null
    )

    @Serializable
    data class Encounter(
        val chance: Int? = null,
        val method: NameUrlObject? = null,
        @SerialName("min_level") val minLevel: Int? = null,
        @SerialName("max_level") val maxLevel: Int? = null,
    )
}
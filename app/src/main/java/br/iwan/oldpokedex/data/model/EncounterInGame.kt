package br.iwan.oldpokedex.data.model

import kotlinx.serialization.Serializable

@Serializable
data class EncounterInGame(
    val method: String? = null,
    val condition: String? = null,
    val chance: Int? = null,
    val minLevel: Int? = null,
    val maxLevel: Int? = null
)

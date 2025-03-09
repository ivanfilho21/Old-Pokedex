package br.iwan.oldpokedex.data.model

import kotlinx.serialization.Serializable

@Serializable
data class VersionDetails(
    val version: String? = null,
    val encounters: List<EncounterInGame>? = null
)

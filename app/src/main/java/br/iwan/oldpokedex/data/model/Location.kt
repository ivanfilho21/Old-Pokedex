package br.iwan.oldpokedex.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val area: String? = null,
    val versions: List<VersionDetails>? = null
)
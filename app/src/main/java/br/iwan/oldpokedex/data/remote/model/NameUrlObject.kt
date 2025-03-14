package br.iwan.oldpokedex.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class NameUrlObject(
    val name: String? = null,
    val url: String? = null
)
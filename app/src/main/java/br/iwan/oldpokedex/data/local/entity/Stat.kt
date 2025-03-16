package br.iwan.oldpokedex.data.local.entity

import kotlinx.serialization.Serializable

@Serializable
data class Stat(
    val name: String? = null,
    val value: Int? = null
)

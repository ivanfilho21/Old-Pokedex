package br.iwan.oldpokedex.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class PokemonListResponse(
    val results: List<PokemonResponse.Data>? = null
)
package br.iwan.oldpokedex.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data object HomeScreen

@Serializable
data class PokemonDetailsScreen(val pokemonName: String)
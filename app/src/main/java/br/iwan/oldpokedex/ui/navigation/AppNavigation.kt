package br.iwan.oldpokedex.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data object HomeScreen

@Serializable
data class PokemonDetailsScreen(val id: Int)

@Serializable
data class PokemonLocationsScreen(val id: Int)
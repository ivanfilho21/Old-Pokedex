package br.iwan.oldpokedex.ui.view_model

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import br.iwan.oldpokedex.data.local.entity.PokemonLocationEntity

class LocationsLayoutViewModel : ViewModel() {
    val pokemonLocations = mutableStateListOf<PokemonLocationEntity>()
}
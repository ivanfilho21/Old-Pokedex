package br.iwan.oldpokedex.ui.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import br.iwan.oldpokedex.data.local.entity.PokemonEntity

class DetailsLayoutViewModel : ViewModel() {
    var currentId: Int? = null
    var loading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var pokemonData by mutableStateOf<PokemonEntity?>(null)

    fun calculateUnits(unit: Int): String {
        return unit.toDouble().div(10.0).toString()
    }
}
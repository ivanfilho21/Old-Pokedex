package br.iwan.oldpokedex.ui.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import br.iwan.oldpokedex.data.local.entity.PokemonEntity

class HomeLayoutViewModel : ViewModel() {
    var error by mutableStateOf<String?>(null)
    var loading by mutableStateOf(false)
    var searchBarQuery by mutableStateOf("")
    private val originalPokemonList = mutableStateListOf<PokemonEntity>()
    val pokemonList = mutableStateListOf<PokemonEntity>()

    fun updatePokemonList(list: List<PokemonEntity>) {
        originalPokemonList.run {
            clear()
            addAll(list)
        }
    }

    fun updateFilter() {
        pokemonList.run {
            clear()
            addAll(
                originalPokemonList.filter {
                    it.name?.contains(searchBarQuery, true) == true
                }
            )
        }
    }
}
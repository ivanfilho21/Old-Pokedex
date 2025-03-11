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
    var searchBarExpanded by mutableStateOf(false)
    var searchBarQuery by mutableStateOf("")
    val suggestions = mutableStateListOf<String>()
    val pokemonList = mutableStateListOf<PokemonEntity>()

    fun updateSuggestions(list: List<String>) {
        suggestions.run {
            clear()
            addAll(list)
        }
    }

    fun updatePokemonList(list: List<PokemonEntity>) {
        pokemonList.run {
            clear()
            addAll(list)
        }
    }

    fun clearSuggestions() {
        suggestions.clear()
    }

    fun onSearchBarAction(id: Int, navigationAction: (Int) -> Unit) {
        clearSuggestions()
        navigationAction(id)
    }
}
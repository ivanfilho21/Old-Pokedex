package br.iwan.oldpokedex.ui.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import br.iwan.oldpokedex.data.local.entity.PokemonEntity
import br.iwan.oldpokedex.ui.SortMode

class HomeLayoutViewModel : ViewModel() {
    var error by mutableStateOf<String?>(null)
    var loading by mutableStateOf(false)
    var searchBarQuery by mutableStateOf("")
    var currentSortingMode by mutableStateOf(SortMode.NUMBER)
    private val originalPokemonList = mutableStateListOf<PokemonEntity>()
    val pokemonList = mutableStateListOf<PokemonEntity>()

    fun updatePokemonList(list: List<PokemonEntity>) {
        originalPokemonList.run {
            clear()
            addAll(list)
        }

        updateFilter()
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

        updateSorting()
    }

    fun toggleSortingOption() {
        currentSortingMode = when (currentSortingMode) {
            SortMode.NUMBER -> SortMode.NAME
            SortMode.NAME -> SortMode.FAVORITE
            SortMode.FAVORITE -> SortMode.NUMBER
        }
    }

    fun updateSorting() {
        when (currentSortingMode) {
            SortMode.NUMBER -> pokemonList.sortBy { it.id }
            SortMode.NAME -> pokemonList.sortBy { it.name }
            SortMode.FAVORITE ->
                pokemonList.sortWith(
                    compareByDescending<PokemonEntity> { it.favorite }.thenBy { it.id }
                )
        }
    }
}
package br.iwan.oldpokedex.ui.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.iwan.oldpokedex.data.local.entity.PokemonEntity
import br.iwan.oldpokedex.data.use_case.PokemonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: PokemonUseCase
) : ViewModel() {
    var searchBarExpanded by mutableStateOf(false)
    var searchBarQuery by mutableStateOf("")

    private var _pokemonListSF = MutableStateFlow<List<PokemonEntity>>(emptyList())
    val pokemonListSF: StateFlow<List<PokemonEntity>> = _pokemonListSF
    private var _suggestionsSF = MutableStateFlow<List<String>>(emptyList())
    val suggestionsSF: StateFlow<List<String>> = _suggestionsSF

    fun listAllPokemon() {
        viewModelScope.launch {
            val res = useCase.listAll()

            _pokemonListSF.value = res
        }
    }

    fun searchByName() {
        viewModelScope.launch {
            val res = useCase.searchByName(searchBarQuery)

            _suggestionsSF.value = res.mapNotNull {
                it.name
            }
        }
    }

    fun clearSuggestions() {
        _suggestionsSF.value = emptyList()
    }

    fun onSearchBarAction(name: String, navigationAction: (String) -> Unit) {
        clearSuggestions()
        navigationAction(name)
    }
}
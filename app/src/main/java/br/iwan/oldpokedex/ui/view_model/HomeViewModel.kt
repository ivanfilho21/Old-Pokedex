package br.iwan.oldpokedex.ui.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.iwan.oldpokedex.data.local.entity.PokemonEntity
import br.iwan.oldpokedex.data.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {
    var searchBarExpanded by mutableStateOf(false)
    var searchBarQuery by mutableStateOf("")

    private var _pokemonListSF = MutableStateFlow<List<PokemonEntity>>(arrayListOf())
    val pokemonListSF: StateFlow<List<PokemonEntity>> = _pokemonListSF

    fun listAllPokemon() {
        viewModelScope.launch {
            val res = repository.listAll()

            _pokemonListSF.value = res
        }
    }
}
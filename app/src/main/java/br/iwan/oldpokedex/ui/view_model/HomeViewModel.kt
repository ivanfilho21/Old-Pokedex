package br.iwan.oldpokedex.ui.view_model

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
    private val _pokemonListSF = MutableStateFlow<List<PokemonEntity>>(emptyList())
    private val _suggestionsSF = MutableStateFlow<List<String>>(emptyList())
    val pokemonListSF: StateFlow<List<PokemonEntity>> = _pokemonListSF
    val suggestionsSF: StateFlow<List<String>> = _suggestionsSF

    fun listAllPokemon() {
        viewModelScope.launch {
            val res = useCase.listAll()

            _pokemonListSF.value = res
        }
    }

    fun searchByName(query: String) {
        viewModelScope.launch {
            val res = useCase.searchByName(query)

            _suggestionsSF.value = res.mapNotNull {
                it.name
            }
        }
    }
}
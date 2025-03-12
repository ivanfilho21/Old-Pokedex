package br.iwan.oldpokedex.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.iwan.oldpokedex.data.local.entity.PokemonEntity
import br.iwan.oldpokedex.data.model.UiResponse
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
    private val _pokemonListSF = MutableStateFlow<UiResponse<List<PokemonEntity>>>(UiResponse.None)
    private val _suggestionsSF = MutableStateFlow<UiResponse<List<String>>>(UiResponse.None)
    val pokemonListSF: StateFlow<UiResponse<List<PokemonEntity>>> = _pokemonListSF
    val suggestionsSF: StateFlow<UiResponse<List<String>>> = _suggestionsSF

    fun listAllPokemon() {
        viewModelScope.launch {
            _pokemonListSF.value = useCase.listAll()
        }
    }

    fun searchByName(query: String) {
        viewModelScope.launch {
            val res = useCase.getSuggestionsFromQuery(query)

            _suggestionsSF.value = res.run {
                if (this.isEmpty()) UiResponse.Error("")
                else UiResponse.Success(
                    mapNotNull {
                        it.name
                    }
                )
            }
        }
    }
}
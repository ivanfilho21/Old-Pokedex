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
class DetailsViewModel @Inject constructor(
    private val useCase: PokemonUseCase
) : ViewModel() {
    private var _pokemonDataSF = MutableStateFlow<UiResponse<PokemonEntity>>(UiResponse.None)
    val pokemonDataSF: StateFlow<UiResponse<PokemonEntity>> = _pokemonDataSF

    fun getDetails(id: Int) {
        viewModelScope.launch {
            _pokemonDataSF.value = useCase.findById(id)
        }
    }
}
package br.iwan.oldpokedex.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.iwan.oldpokedex.data.local.entity.PokemonLocationEntity
import br.iwan.oldpokedex.data.model.UiResponse
import br.iwan.oldpokedex.data.use_case.PokemonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationsViewModel @Inject constructor(
    private val useCase: PokemonUseCase
) : ViewModel() {
    private val _locationsSF = MutableStateFlow<UiResponse<PokemonLocationEntity>>(UiResponse.None)
    val locationsSF: StateFlow<UiResponse<PokemonLocationEntity>> = _locationsSF

    fun getLocationsByPokemonId(id: Int) {
        viewModelScope.launch {
            _locationsSF.value = useCase.findAllLocationsByPokemonId(id)
        }
    }
}
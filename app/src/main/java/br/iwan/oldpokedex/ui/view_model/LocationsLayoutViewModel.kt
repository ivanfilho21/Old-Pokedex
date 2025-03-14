package br.iwan.oldpokedex.ui.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import br.iwan.oldpokedex.data.local.entity.PokemonLocationEntity

class LocationsLayoutViewModel : ViewModel() {
    var currentId: Int? = null
    var loading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var locationData by mutableStateOf<PokemonLocationEntity?>(null)
}
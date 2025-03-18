package br.iwan.oldpokedex.ui.view_model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import br.iwan.oldpokedex.data.local.entity.PokemonLocationEntity
import br.iwan.oldpokedex.data.model.EncounterInGame
import br.iwan.oldpokedex.data.model.Location

class LocationsLayoutViewModel : ViewModel() {
    var currentId: Int? = null
    var loading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var pokemonName by mutableStateOf<String?>(null)
    var locationData by mutableStateOf<PokemonLocationEntity?>(null)

    fun mergeEncountersInAreaByVersion(area: Location): Map<EncounterInGame, List<String>> {
        val same = mutableMapOf<EncounterInGame, List<String>>()

        area.versions?.let {
            it.forEach { v1 ->
                it.forEach { v2 ->
                    v1.encounters?.forEach { v1e ->
                        v2.encounters?.forEach { v2e ->
                            if (v1e sameAs v2e) {
                                same[v1e] = (same[v1e] ?: emptyList()).toMutableList().apply {
                                    val elem = v1.version.orEmpty()
                                    if (!this.contains(elem)) {
                                        add(elem)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        same.forEach {
            Log.d("MERGE", "[${it.key.string()}] -> ${it.value}")
        }

        return same
    }

    private infix fun EncounterInGame.sameAs(a: EncounterInGame) = this.run {
        method == a.method && condition == a.condition && chance == a.chance && minLevel == a.minLevel && maxLevel == a.maxLevel
    }

    private fun EncounterInGame.string() = this.run {
        "$method,$condition,$chance,$minLevel,$maxLevel"
    }
}
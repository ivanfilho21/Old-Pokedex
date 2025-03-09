package br.iwan.oldpokedex.ui.helper

import br.iwan.oldpokedex.data.model.EncounterInGame

object PokemonHelper {
    fun mergePokemonEncounterLevel(encounter: EncounterInGame) = encounter.run {
        if (minLevel == maxLevel) minLevel?.toString().orEmpty()
        else "${minLevel?.toString() ?: ""} - ${maxLevel?.toString() ?: ""}"
    }
}
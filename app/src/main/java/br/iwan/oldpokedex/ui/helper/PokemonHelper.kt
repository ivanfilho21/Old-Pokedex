package br.iwan.oldpokedex.ui.helper

import android.util.Log
import br.iwan.oldpokedex.data.model.EncounterInGame
import br.iwan.oldpokedex.data.model.Location

object PokemonHelper {
    private fun getChanceText(encounter: EncounterInGame) = encounter.run {
        if (chance == 100) ""
        else "$chance%"
    }

    private fun mergePokemonEncounterLevel(encounter: EncounterInGame) = encounter.run {
        if (minLevel == maxLevel) minLevel?.toString().orEmpty()
        else "${minLevel?.toString() ?: ""} - ${maxLevel?.toString() ?: ""}"
    }

    fun String.capitalizeWords() =
        this.split('-').joinToString(" ") {
            it.replaceFirstChar { c -> c.uppercaseChar() }
        }

    fun String.formatPokemonName() =
        this.replace("-f", "♀").replace("-m", "♂").capitalizeWords()

    fun getEncounterText(encounter: EncounterInGame): String = encounter.run {
        if (chance == 100) {
            return "LV " + mergePokemonEncounterLevel(this)
        }

        return getChanceText(encounter) + " chance of finding it at LV " + mergePokemonEncounterLevel(encounter) + "."
    }

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
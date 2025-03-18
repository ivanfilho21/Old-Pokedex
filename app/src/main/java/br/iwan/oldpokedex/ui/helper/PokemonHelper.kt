package br.iwan.oldpokedex.ui.helper

import br.iwan.oldpokedex.data.model.EncounterInGame

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

        return getChanceText(encounter) + " chance of finding it at LV " + mergePokemonEncounterLevel(
            encounter
        ) + "."
    }

    fun List<String>.joinWithComma(transform: ((String) -> String)): String {
        val sb = StringBuilder()

        this.forEachIndexed { i, text ->
            sb.append(transform(text)).append(
                when (i) {
                    size - 1 -> ""
                    size - 2 -> " and "
                    else -> ", "
                }
            )
        }

        return sb.toString()
    }
}
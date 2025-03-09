package br.iwan.oldpokedex.ui.helper

import androidx.compose.ui.graphics.Color
import br.iwan.oldpokedex.data.local.entity.PokemonEntity
import br.iwan.oldpokedex.ui.theme.blue800
import br.iwan.oldpokedex.ui.theme.green800
import br.iwan.oldpokedex.ui.theme.orange800
import br.iwan.oldpokedex.ui.theme.purple800

object ColorHelper {
    fun getColorByPokemonType(type: String?) = when (type?.lowercase()) {
        "grass" -> green800
        "fire" -> orange800
        "water" -> blue800
        "poison" -> purple800
        else -> Color(0xFF959595)
    }

    fun getColorListByPokemon(pokemon: PokemonEntity?): List<Color> {
        val color1 = pokemon?.type1?.let { getColorByPokemonType(it) } ?: Color(0xFF959595)
        val color2 = pokemon?.type2?.let { getColorByPokemonType(it) } ?: color1.copy(alpha = 0.65f)

        return listOf(
            color2,
            color1,
            color2
        )
    }
}
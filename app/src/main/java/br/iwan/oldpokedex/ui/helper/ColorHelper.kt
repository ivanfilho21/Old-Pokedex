package br.iwan.oldpokedex.ui.helper

import androidx.compose.ui.graphics.Color
import br.iwan.oldpokedex.data.local.entity.PokemonEntity
import br.iwan.oldpokedex.ui.theme.amberA400
import br.iwan.oldpokedex.ui.theme.blue800
import br.iwan.oldpokedex.ui.theme.brown600
import br.iwan.oldpokedex.ui.theme.deepOrange700
import br.iwan.oldpokedex.ui.theme.deepPurple700
import br.iwan.oldpokedex.ui.theme.gray600
import br.iwan.oldpokedex.ui.theme.green800
import br.iwan.oldpokedex.ui.theme.indigo400
import br.iwan.oldpokedex.ui.theme.indigo700
import br.iwan.oldpokedex.ui.theme.lightBlue700
import br.iwan.oldpokedex.ui.theme.lime800
import br.iwan.oldpokedex.ui.theme.orange800
import br.iwan.oldpokedex.ui.theme.pink500
import br.iwan.oldpokedex.ui.theme.purple800
import br.iwan.oldpokedex.ui.theme.yellow800

object ColorHelper {
    fun getColorByPokemonType(type: String?) = when (type?.lowercase()) {
        "bug" -> lime800
        "grass" -> green800
        "fire" -> orange800
        "water" -> blue800
        "poison" -> purple800
        "ice" -> lightBlue700
        "dragon" -> deepPurple700
        "psychic" -> pink500
        "ghost" -> indigo700
        "rock" -> brown600
        "ground" -> amberA400
        "fighting" -> deepOrange700
        "electric" -> yellow800
        "flying" -> indigo400
        else -> gray600
    }

    fun getColorListByPokemon(pokemon: PokemonEntity?): List<Color> {
        val color1 = pokemon?.type1?.let { getColorByPokemonType(it) } ?: gray600
        val color2 = pokemon?.type2?.let { getColorByPokemonType(it) } ?: color1.copy(alpha = 0.65f)

        return listOf(
            color2,
            color1,
            color2
        )
    }
}
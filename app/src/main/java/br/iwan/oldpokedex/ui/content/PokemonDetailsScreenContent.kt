package br.iwan.oldpokedex.ui.content

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import br.iwan.oldpokedex.ui.view_model.DetailsViewModel

@Composable
fun PokemonDetailsScreenContent(viewModel: DetailsViewModel) {
    val pokemonData by viewModel.pokemonDataSF.collectAsState()

    Text(text = "${pokemonData?.id} - ${pokemonData?.name}\n${pokemonData?.description}")
}
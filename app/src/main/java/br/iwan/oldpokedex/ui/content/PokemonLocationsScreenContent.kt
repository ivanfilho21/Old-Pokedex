package br.iwan.oldpokedex.ui.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import br.iwan.oldpokedex.R
import br.iwan.oldpokedex.data.local.entity.PokemonLocationEntity
import br.iwan.oldpokedex.data.model.EncounterInGame
import br.iwan.oldpokedex.data.model.Location
import br.iwan.oldpokedex.data.model.VersionDetails
import br.iwan.oldpokedex.ui.helper.PokemonHelper
import br.iwan.oldpokedex.ui.helper.PokemonHelper.capitalizeWords
import br.iwan.oldpokedex.ui.helper.PokemonHelper.formatPokemonName
import br.iwan.oldpokedex.ui.helper.PokemonHelper.joinWithComma
import br.iwan.oldpokedex.ui.theme.AppTypography
import br.iwan.oldpokedex.ui.view_model.LocationsLayoutViewModel
import java.util.UUID

@Preview
@Composable
private fun Preview() {
    DefaultPreview {
        PokemonLocationsScreenContent(
            viewModel = viewModel<LocationsLayoutViewModel>().apply {
                pokemonName = "Pokémon"

                locationData = PokemonLocationEntity(
                    UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
                    0,
                    listOf(
                        Location(
                            "seafoam-islands-b4f",
                            listOf(
                                VersionDetails(
                                    "red", listOf(
                                        EncounterInGame("good-rod", null, 50, 10, 10),
                                        EncounterInGame("surf", null, 30, 20, 25),
                                    )
                                ),
                                VersionDetails(
                                    "blue", listOf(
                                        EncounterInGame("good-rod", null, 50, 10, 10),
                                        EncounterInGame("surf", null, 30, 20, 25),
                                    )
                                ),
                                VersionDetails(
                                    "yellow", listOf(
                                        EncounterInGame("walk", null, 50, 10, 10),
                                        EncounterInGame("walk", null, 28, 26, 29),
                                        EncounterInGame("surf", null, 30, 20, 25),
                                    )
                                )
                            )
                        ),
                        Location(
                            "viridian-city-area",
                            listOf(
                                VersionDetails(
                                    "red", listOf(
                                        EncounterInGame("gift", null, 30, 10, 10),
                                        EncounterInGame("surf", null, 20, 20, 25),
                                    )
                                ),
                                VersionDetails(
                                    "blue", listOf(
                                        EncounterInGame("old-rod", null, 50, 10, 10),
                                        EncounterInGame("surf", null, 30, 20, 25),
                                    )
                                ),
                                VersionDetails(
                                    "silver", listOf(
                                        EncounterInGame("super-rod", null, 50, 10, 10),
                                        EncounterInGame("surf", null, 30, 20, 25),
                                    )
                                )
                            )
                        ),
                    )
                )
            },
            onTryAgainClick = {}
        )
    }
}

@Preview
@Composable
private fun PreviewNoLocations() {
    DefaultPreview {
        PokemonLocationsScreenContent(
            viewModel = viewModel(),
            onTryAgainClick = {}
        )
    }
}

@Composable
fun PokemonLocationsScreenContent(
    viewModel: LocationsLayoutViewModel,
    onTryAgainClick: () -> Unit
) {
    MainLayout(
        isLoading = viewModel.loading,
        error = viewModel.error,
        onTryAgainClick = onTryAgainClick
    ) {
        Content(viewModel)
    }
}

@Composable
private fun Content(viewModel: LocationsLayoutViewModel) {
    val locations = viewModel.locationData?.locations.orEmpty()

    if (locations.isEmpty()) {
        EmptyLocationContent()
        return
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val (titleRef, listRef) = createRefs()

        Text(
            text = stringResource(
                R.string.title_locations_screen_poke_name_value,
                viewModel.pokemonName?.formatPokemonName().orEmpty()
            ),
            style = AppTypography.titleLarge,
            modifier = Modifier.constrainAs(titleRef) {
                top.linkTo(parent.top, 16.dp)
                start.linkTo(parent.start, 16.dp)
                end.linkTo(parent.end, 16.dp)
                width = Dimension.fillToConstraints
            }
        )

        LazyColumn(
            userScrollEnabled = false,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .heightIn(max = locations.size.times(1_000).dp)
                .constrainAs(listRef) {
                    top.linkTo(titleRef.bottom, 16.dp)
                    centerHorizontallyTo(parent)
                    width = Dimension.fillToConstraints
                }
        ) {
            items(items = locations) { region ->
                Text(
                    text = region.area?.capitalizeWords().orEmpty(),
                    style = AppTypography.titleMedium.copy(
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillParentMaxWidth()
                )

                viewModel.mergeEncountersInAreaByVersion(region).forEach { (encounter, versions) ->
                    Text(
                        text = versions.joinWithComma(LocalContext.current) {
                            it.capitalizeWords()
                        },
                        style = AppTypography.titleSmall,
                        modifier = Modifier.fillParentMaxWidth()
                    )

                    Text(
                        text = " • " + encounter.method?.capitalizeWords()
                            .orEmpty() + ": " + PokemonHelper.getEncounterText(encounter),
                        style = AppTypography.bodyMedium,
                        modifier = Modifier.fillParentMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyLocationContent() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val (titleRef) = createRefs()

        Text(
            text = "This Pokémon can't be found in any games.",
            style = AppTypography.bodyLarge,
            modifier = Modifier.constrainAs(titleRef) {
                linkTo(
                    top = parent.top,
                    topMargin = 16.dp,
                    bottom = parent.bottom,
                    bottomMargin = 16.dp,
                    start = parent.start,
                    startMargin = 24.dp,
                    end = parent.end,
                    endMargin = 24.dp
                )

                Dimension.fillToConstraints.let {
                    width = it
                    height = it
                }
            }
        )
    }
}
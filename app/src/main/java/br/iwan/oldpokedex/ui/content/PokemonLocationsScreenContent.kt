package br.iwan.oldpokedex.ui.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import br.iwan.oldpokedex.data.local.entity.PokemonLocationEntity
import br.iwan.oldpokedex.data.model.EncounterInGame
import br.iwan.oldpokedex.data.model.VersionDetails
import br.iwan.oldpokedex.ui.helper.PokemonHelper
import br.iwan.oldpokedex.ui.helper.PokemonHelper.capitalizeWords
import br.iwan.oldpokedex.ui.theme.AppTypography
import br.iwan.oldpokedex.ui.view_model.LocationsLayoutViewModel
import java.util.UUID

@Preview
@Composable
private fun Preview() {
    DefaultPreview {
        PokemonLocationsScreenContent(
            viewModel = viewModel<LocationsLayoutViewModel>().apply {
                pokemonLocations.addAll(listOf(
                    PokemonLocationEntity(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"), 60, "seafoam-islands-b4f", listOf(
                        VersionDetails("red", listOf(
                            EncounterInGame("good-rod", null, 50, 10, 10),
                            EncounterInGame("surf", null, 30, 20, 25),
                        )),
                        VersionDetails("blue", listOf(
                            EncounterInGame("good-rod", null, 50, 10, 10),
                            EncounterInGame("surf", null, 30, 20, 25),
                        )),
                        VersionDetails("yellow", listOf(
                            EncounterInGame("walk", null, 50, 10, 10),
                            EncounterInGame("walk", null, 28, 26, 29),
                            EncounterInGame("surf", null, 30, 20, 25),
                        ))
                    )),
                    PokemonLocationEntity(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"), 60, "viridian-city-area", listOf(
                        VersionDetails("red", listOf(
                            EncounterInGame("gift", null, 30, 10, 10),
                            EncounterInGame("surf", null, 20, 20, 25),
                        )),
                        VersionDetails("blue", listOf(
                            EncounterInGame("old-rod", null, 50, 10, 10),
                            EncounterInGame("surf", null, 30, 20, 25),
                        )),
                        VersionDetails("silver", listOf(
                            EncounterInGame("super-rod", null, 50, 10, 10),
                            EncounterInGame("surf", null, 30, 20, 25),
                        ))
                    )),
                ))
            }
        )
    }
}

@Composable
fun PokemonLocationsScreenContent(viewModel: LocationsLayoutViewModel) {
    val locations = viewModel.pokemonLocations

    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .verticalScroll(rememberScrollState())
    ) {
        val (titleRef, listRef) = createRefs()

        Text(
            text = "Locations",
            style = AppTypography.titleLarge,
            modifier = Modifier.constrainAs(titleRef) {
                top.linkTo(parent.top)
                centerHorizontallyTo(parent)
                width = Dimension.fillToConstraints
            }
        )

        LazyColumn(
            userScrollEnabled = false,
            verticalArrangement = Arrangement.spacedBy(16.dp),
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

                LazyColumn(
                    userScrollEnabled = false,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .heightIn(max = 1_000.dp)
                ) {
                    items(items = region.versions.orEmpty()) { game ->
                        Text(
                            text = game.version?.capitalizeWords().orEmpty(),
                            style = AppTypography.titleSmall,
                            modifier = Modifier.fillParentMaxWidth()
                        )

                        LazyColumn(
                            userScrollEnabled = false,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .heightIn(max = 1_000.dp)
                        ) {
                            items(items = game.encounters.orEmpty()) { encounter ->
                                ConstraintLayout(modifier = Modifier.fillParentMaxWidth()) {
                                    val (methodRef, lvRef) = createRefs()

                                    Text(
                                        text = " • " + encounter.method?.capitalizeWords().orEmpty(),
                                        style = AppTypography.bodyMedium,
                                        modifier = Modifier.constrainAs(methodRef) {
                                            top.linkTo(parent.top)
                                            start.linkTo(parent.start, 8.dp)
                                            end.linkTo(parent.end)
                                            width = Dimension.fillToConstraints
                                        }
                                    )

                                    Text(
                                        text = encounter.chance?.toString().orEmpty() + "% chance of finding LV " + PokemonHelper.mergePokemonEncounterLevel(encounter) + ".",
                                        style = AppTypography.bodySmall,
                                        modifier = Modifier.constrainAs(lvRef) {
                                            top.linkTo(methodRef.bottom, 4.dp)
                                            bottom.linkTo(parent.bottom)
                                            start.linkTo(methodRef.start, 12.dp)
                                            end.linkTo(methodRef.end)
                                            width = Dimension.fillToConstraints
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
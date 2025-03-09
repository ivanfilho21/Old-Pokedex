package br.iwan.oldpokedex.ui.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.atMost
import br.iwan.oldpokedex.data.local.entity.PokemonLocationEntity
import br.iwan.oldpokedex.ui.theme.AppTypography
import br.iwan.oldpokedex.ui.theme.textPrimaryColor

@Composable
fun PokemonLocationsScreenContent() {
    val locations: List<PokemonLocationEntity>? = null

    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
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
                .heightIn(max = (locations?.size?.times(1_000) ?: 0).dp)
                .constrainAs(listRef) {
                    top.linkTo(titleRef.bottom, 16.dp)
                    centerHorizontallyTo(parent)
                    width = Dimension.fillToConstraints
                }
        ) {
            items(items = locations.orEmpty()) { region ->
                Text(
                    text = region.area.orEmpty(),
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
                    region.versions?.groupBy { it.version }?.forEach { (game, encounters) ->
                        item {
                            Text(
                                text = game.orEmpty(),
                                style = AppTypography.titleSmall,
                                modifier = Modifier.fillParentMaxWidth()
                            )
                        }

                        items(items = encounters) { encounter ->
                            ConstraintLayout(modifier = Modifier.fillParentMaxWidth()) {
                                val (bulletRef, placeRef, lvRef, chanceRef, div1, div2) = createRefs()

                                Box(
                                    modifier = Modifier
                                        .constrainAs(bulletRef) {
                                            centerVerticallyTo(parent)
                                            start.linkTo(parent.start, 8.dp)

                                            4.dp
                                                .asDimension()
                                                .let {
                                                    width = it
                                                    height = it
                                                }
                                        }
                                        .background(textPrimaryColor, CircleShape)
                                )

                                InfoLayoutRow(
                                    info = encounter.version.orEmpty(),
                                    label = "Location",
                                    modifier = Modifier.constrainAs(placeRef) {
                                        top.linkTo(parent.top)
                                        start.linkTo(bulletRef.start, 8.dp)
                                        end.linkTo(lvRef.start, 2.dp)
                                        width = Dimension.fillToConstraints
                                    }
                                )

                                VerticalDivider(
                                    modifier = Modifier.constrainAs(div1) {
                                        start.linkTo(placeRef.end)
                                        end.linkTo(lvRef.start)
                                        centerVerticallyTo(parent)
                                        height = Dimension.fillToConstraints.atMost(24.dp)
                                    }
                                )

                                InfoLayoutRow(
                                    info = "_0_",
                                    label = "Level",
                                    modifier = Modifier.constrainAs(lvRef) {
                                        top.linkTo(placeRef.top)
                                        start.linkTo(placeRef.end, 2.dp)
                                        end.linkTo(chanceRef.start, 2.dp)
                                        width = Dimension.fillToConstraints
                                    }
                                )

                                VerticalDivider(
                                    modifier = Modifier.constrainAs(div2) {
                                        start.linkTo(lvRef.end)
                                        end.linkTo(chanceRef.start)
                                        centerVerticallyTo(parent)
                                        height = Dimension.fillToConstraints.atMost(24.dp)
                                    }
                                )

                                /*InfoLayoutRow(
                                    info = PokemonHelper.mergePokemonEncounterChange(encounter),
                                    label = "Encounter rate",
                                    modifier = Modifier.constrainAs(chanceRef) {
                                        top.linkTo(lvRef.top)
                                        start.linkTo(lvRef.end, 2.dp)
                                        end.linkTo(parent.end)
                                        width = Dimension.fillToConstraints
                                    }
                                )*/
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoLayoutRow(info: String, label: String, modifier: Modifier) {

}
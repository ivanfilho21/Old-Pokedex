package br.iwan.oldpokedex.ui.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.Visibility
import androidx.constraintlayout.compose.atLeast
import androidx.lifecycle.viewmodel.compose.viewModel
import br.iwan.oldpokedex.data.local.entity.PokemonEntity
import br.iwan.oldpokedex.ui.helper.ColorHelper
import br.iwan.oldpokedex.ui.theme.AppTypography
import br.iwan.oldpokedex.ui.theme.backgroundColor
import br.iwan.oldpokedex.ui.view_model.DetailsLayoutViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Preview
@Composable
private fun Preview() {
    DefaultPreview {
        PokemonDetailsScreenContent(
            viewModel = viewModel<DetailsLayoutViewModel>().apply {
                pokemonData = PokemonEntity(
                    0,
                    "Pokémon name",
                    LoremIpsum(10).values.joinToString(" "),
                    "Type 1",
                    null,
                    7,
                    69
                )
            }
        )
    }
}

@Composable
fun PokemonDetailsScreenContent(viewModel: DetailsLayoutViewModel) {
    val pokemonData = viewModel.pokemonData

    var bgColors by remember {
        mutableStateOf(ColorHelper.getColorListByPokemon(pokemonData))
    }

    LaunchedEffect(pokemonData) {
        bgColors = ColorHelper.getColorListByPokemon(pokemonData)
    }

    ConstraintLayout(
        modifier = Modifier
            .background(Brush.sweepGradient(bgColors))
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val (idRef, imgRef, mainContentRef, emptyBottomRef) = createRefs()
        val mainContentBgColor = Color(0xDDF5F5F5)
        val bgRadius = 32.dp

        ConstraintLayout(
            modifier = Modifier
                .background(
                    mainContentBgColor,
                    RoundedCornerShape(topStart = bgRadius, topEnd = bgRadius)
                )
                .constrainAs(mainContentRef) {
                    top.linkTo(imgRef.bottom, -(78).dp)
                    centerHorizontallyTo(parent)
                    width = Dimension.fillToConstraints
                }
        ) {
            val (nameRef, descRef, typesRef, aboutRef, locationRef) = createRefs()
            val mainContentGuidelineStart = createGuidelineFromStart(16.dp)
            val mainContentGuidelineEnd = createGuidelineFromEnd(16.dp)

            Text(
                text = pokemonData?.name.orEmpty(),
                style = AppTypography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.constrainAs(nameRef) {
                    top.linkTo(parent.top, 64.dp)
                    start.linkTo(mainContentGuidelineStart)
                    end.linkTo(mainContentGuidelineEnd)
                    width = Dimension.fillToConstraints
                }
            )

            ConstraintLayout(
                modifier = Modifier.constrainAs(typesRef) {
                    top.linkTo(nameRef.bottom, 16.dp)
                    centerHorizontallyTo(nameRef)
                    width = Dimension.fillToConstraints
                }
            ) {
                val (tp1, tp2, div) = createRefs()

                TypeLayout(
                    type = pokemonData?.type1,
                    modifier = Modifier.constrainAs(tp1) {}
                )

                val hasSecondType = pokemonData?.type2 != null

                Box(modifier = Modifier.constrainAs(div) {
                    if (hasSecondType) {
                        width = 16.dp.asDimension()
                    }
                })

                TypeLayout(
                    type = pokemonData?.type2,
                    modifier = Modifier.constrainAs(tp2) {
                        visibility =
                            if (hasSecondType) Visibility.Visible
                            else Visibility.Gone
                    }
                )

                createHorizontalChain(tp1, div, tp2, chainStyle = ChainStyle.Packed)
            }

            Text(
                text = pokemonData?.description.orEmpty(),
                style = AppTypography.bodyLarge,
                modifier = Modifier.constrainAs(descRef) {
                    top.linkTo(typesRef.bottom, 16.dp)
                    centerHorizontallyTo(nameRef)
                    width = Dimension.fillToConstraints
                }
            )

            ConstraintLayout(
                modifier = Modifier.constrainAs(aboutRef) {
                    top.linkTo(descRef.bottom, 16.dp)
                    centerHorizontallyTo(descRef)
                    width = Dimension.fillToConstraints
                }
            ) {
                val (heightRef, div, weightRef) = createRefs()

                InfoLayout(
                    info = viewModel.calculateUnits(pokemonData?.height ?: 0) + " m",
                    label = "Height",
                    modifier = Modifier.constrainAs(heightRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(weightRef.start, 2.dp)
                        width = Dimension.fillToConstraints
                    }
                )

                VerticalDivider(
                    modifier = Modifier.constrainAs(div) {
                        centerTo(parent)
                        height = Dimension.fillToConstraints
                    }
                )

                InfoLayout(
                    info = viewModel.calculateUnits(pokemonData?.weight ?: 0) + " kg",
                    label = "Weight",
                    modifier = Modifier.constrainAs(weightRef) {
                        top.linkTo(heightRef.top)
                        start.linkTo(heightRef.end, 2.dp)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
                )
            }

            PokemonLocationLayout(
                modifier = Modifier.constrainAs(locationRef) {
                    top.linkTo(aboutRef.bottom, 24.dp)
                    bottom.linkTo(parent.bottom)
                    centerHorizontallyTo(aboutRef)

                    width = Dimension.fillToConstraints
                }
            )
        }

        Text(
            "# ${pokemonData?.id}",
            style = AppTypography.headlineSmall,
            color = Color(0xFFF5F5F5),
            modifier = Modifier.constrainAs(idRef) {
                top.linkTo(parent.top, 16.dp)
                end.linkTo(parent.end, 16.dp)
            }
        )

        val context = LocalContext.current

        AsyncImage(
            model = ImageRequest.Builder(context)
                .data("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemonData?.id.toString()}.png")
                .crossfade(true)
                .build(),
            contentDescription = null,
            alignment = Alignment.Center,
            contentScale = ContentScale.Fit,
            modifier = Modifier.constrainAs(imgRef) {
                top.linkTo(idRef.bottom)
                centerHorizontallyTo(parent)

                250.dp.asDimension().let {
                    width = it
                    height = it
                }
            }
        )

        Box(
            modifier = Modifier
                .background(mainContentBgColor)
                .constrainAs(emptyBottomRef) {
                    top.linkTo(mainContentRef.bottom)
                    bottom.linkTo(parent.bottom)
                    centerHorizontallyTo(parent)

                    Dimension.fillToConstraints.let {
                        width = it
                        height = it.atLeast(16.dp)
                    }
                }
        )
    }
}

@Composable
private fun TypeLayout(type: String?, modifier: Modifier) {
    Text(
        text = type.orEmpty(),
        style = AppTypography.bodyLarge.copy(
            color = backgroundColor
        ),
        modifier = modifier
            .background(
                color = ColorHelper.getColorByPokemonType(type),
                shape = RoundedCornerShape(50)
            )
            .padding(vertical = 4.dp, horizontal = 12.dp)
    )
}

@Composable
private fun InfoLayout(info: String, label: String, modifier: Modifier) {
    ConstraintLayout(modifier = modifier) {
        val (infoRef, labelRef) = createRefs()

        Text(
            text = info,
            style = AppTypography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.constrainAs(infoRef) {
                top.linkTo(parent.top)
                centerHorizontallyTo(parent)
                width = Dimension.fillToConstraints
            }
        )

        Text(
            text = label,
            style = AppTypography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.constrainAs(labelRef) {
                top.linkTo(infoRef.bottom, 8.dp)
                centerHorizontallyTo(infoRef)
                width = Dimension.fillToConstraints
            }
        )
    }
}

@Composable
private fun PokemonLocationLayout(modifier: Modifier) {
    ConstraintLayout(modifier = modifier) {
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
            modifier = Modifier
                .heightIn(max = 10_000.dp)
                .constrainAs(listRef) {
                    top.linkTo(titleRef.bottom, 16.dp)
                    centerHorizontallyTo(parent)
                    width = Dimension.fillToConstraints
                }
        ) {
            item {
                Text(
                    "Generation + number"
                )
            }

            item {
                Text(
                    "  Game name (Red, Crystal, Emerald, Platinum, Black 2, etc.)"
                )
            }

            item {
                Text(
                    "    Location name + method type (rod, grass, etc.)"
                )
            }

            item {
                Text(
                    "      Encounter rate (i.e from 20% to 24%)"
                )
            }
        }
    }
}
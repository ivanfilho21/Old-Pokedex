package br.iwan.oldpokedex.ui.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.constraintlayout.compose.atMost
import androidx.lifecycle.viewmodel.compose.viewModel
import br.iwan.oldpokedex.data.local.entity.PokemonEntity
import br.iwan.oldpokedex.data.local.entity.Stat
import br.iwan.oldpokedex.ui.helper.ColorHelper
import br.iwan.oldpokedex.ui.helper.PokemonHelper.capitalizeWords
import br.iwan.oldpokedex.ui.helper.PokemonHelper.formatPokemonName
import br.iwan.oldpokedex.ui.theme.AppTypography
import br.iwan.oldpokedex.ui.theme.backgroundColor
import br.iwan.oldpokedex.ui.view_model.DetailsLayoutViewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import kotlinx.coroutines.delay
import java.util.UUID

@Preview
@Composable
private fun Preview() {
    DefaultPreview {
        PokemonDetailsScreenContent(
            viewModel = viewModel<DetailsLayoutViewModel>().apply {
                pokemonData = PokemonEntity(
                    UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
                    0,
                    false,
                    "Pokémon name",
                    LoremIpsum(6).values.joinToString(" "),
                    "water",
                    "dragon",
                    7,
                    69,
                    listOf(
                        Stat("hp", 90),
                        Stat("attack", 25),
                        Stat("defense", 30),
                        Stat("speed", 63),
                        Stat("special-attack", 45),
                        Stat("special-defense", 45),
                    )
                )
            },
            seeLocationsClick = {},
            playCryClick = {},
            onFavoriteClick = { _, _ -> },
            onTryAgainClick = {}
        )
    }
}

@Composable
fun PokemonDetailsScreenContent(
    viewModel: DetailsLayoutViewModel,
    seeLocationsClick: (Int) -> Unit,
    playCryClick: (Int?) -> Unit,
    onFavoriteClick: (Int, Boolean) -> Unit,
    onTryAgainClick: () -> Unit
) {
    MainLayout(
        isLoading = viewModel.loading,
        error = viewModel.error,
        onTryAgainClick = onTryAgainClick
    ) {
        Content(viewModel, seeLocationsClick, playCryClick, onFavoriteClick)
    }
}

@Composable
private fun Content(
    viewModel: DetailsLayoutViewModel,
    seeLocationsClick: (Int) -> Unit,
    playCryClick: (Int?) -> Unit,
    onFavoriteClick: (Int, Boolean) -> Unit
) {
    val pokemonData = viewModel.pokemonData
    val typeOneColor = ColorHelper.getColorByPokemonType(pokemonData?.type1)

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
        val (idRef, imgRef, mainContentRef, emptyBottomRef, locationRef) = createRefs()
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
            val (favRef, cryRef, nameRef, descRef, typesRef, aboutRef, statsRef) = createRefs()
            val mainContentGuidelineStart = createGuidelineFromStart(16.dp)
            val mainContentGuidelineEnd = createGuidelineFromEnd(16.dp)
            
            var cryBtnEnabled by remember {
                mutableStateOf(true)
            }

            LaunchedEffect(cryBtnEnabled) {
                if (cryBtnEnabled) return@LaunchedEffect
                delay(1_000L)
                cryBtnEnabled = true
            }

            Button(
                enabled = cryBtnEnabled,
                onClick = {
                    cryBtnEnabled = false
                    playCryClick(pokemonData?.id)
                },
                contentPadding = PaddingValues(horizontal = 8.dp),
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = typeOneColor
                ),
                modifier = Modifier.constrainAs(cryRef) {
                    top.linkTo(parent.top, 8.dp)
                    end.linkTo(parent.end, 8.dp)
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.PlayArrow,
                    contentDescription = null
                )

                Text(
                    text = "Cry",
                    modifier = Modifier.padding(end = 4.dp)
                )
            }

            ButtonFavoritePokemon(
                pokemon = pokemonData,
                onClick = onFavoriteClick,
                modifier = Modifier.constrainAs(favRef) {
                    centerVerticallyTo(nameRef)
                    start.linkTo(mainContentGuidelineStart)
                    end.linkTo(nameRef.start, 8.dp)
                }
            )

            Text(
                text = pokemonData?.name?.formatPokemonName().orEmpty(),
                style = AppTypography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.constrainAs(nameRef) {
                    top.linkTo(parent.top, 72.dp)
                    start.linkTo(favRef.end)
                    end.linkTo(mainContentGuidelineEnd)
                    width = Dimension.fillToConstraints
                }
            )

            ConstraintLayout(
                modifier = Modifier.constrainAs(typesRef) {
                    top.linkTo(nameRef.bottom, 16.dp)
                    start.linkTo(mainContentGuidelineStart)
                    end.linkTo(mainContentGuidelineEnd)
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
                        visibility = getSecondTypeVisibility(hasSecondType)
                    }
                )

                createHorizontalChain(tp1, div, tp2, chainStyle = ChainStyle.Packed)
            }

            ConstraintLayout(
                modifier = Modifier.constrainAs(aboutRef) {
                    top.linkTo(typesRef.bottom, 16.dp)
                    centerHorizontallyTo(typesRef)
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
                        height = Dimension.fillToConstraints.atMost(24.dp)
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

            Text(
                text = pokemonData?.description.orEmpty(),
                style = AppTypography.bodyLarge,
                modifier = Modifier.constrainAs(descRef) {
                    top.linkTo(aboutRef.bottom, 16.dp)
                    centerHorizontallyTo(aboutRef)
                    width = Dimension.fillToConstraints
                }
            )

            StatsLayout(
                stats = pokemonData?.stats,
                mainColor = typeOneColor,
                modifier = Modifier.constrainAs(statsRef) {
                    top.linkTo(descRef.bottom, 16.dp)
                    bottom.linkTo(parent.bottom)
                    centerHorizontallyTo(descRef)
                    width = Dimension.fillToConstraints
                }
            )
        }

        Text(
            "# ${pokemonData?.id}",
            style = AppTypography.titleSmall,
            color = Color(0xFFF5F5F5),
            modifier = Modifier.constrainAs(idRef) {
                top.linkTo(parent.top, 16.dp)
                end.linkTo(parent.end, 16.dp)
            }
        )

        val context = LocalContext.current
        val id = pokemonData?.id

        val loader = ImageLoader.Builder(context)
            .respectCacheHeaders(false)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .networkCachePolicy(CachePolicy.ENABLED)
            .build()

        AsyncImage(
            model = ImageRequest.Builder(context)
                .data("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png")
                .crossfade(true)
                .networkCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build(),
            imageLoader = loader,
            contentDescription = null,
            alignment = Alignment.Center,
            contentScale = ContentScale.Fit,
            modifier = Modifier.constrainAs(imgRef) {
                top.linkTo(idRef.bottom, 72.dp)
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

        var btnEnabled by remember {
            mutableStateOf(true)
        }

        LaunchedEffect(btnEnabled) {
            if (btnEnabled) return@LaunchedEffect
            delay(500L)
            btnEnabled = true
        }

        Button(
            enabled = btnEnabled,
            onClick = {
                btnEnabled = false
                seeLocationsClick(pokemonData?.id ?: 0)
            },
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = typeOneColor
            ),
            modifier = Modifier.constrainAs(locationRef) {
                linkTo(
                    top = emptyBottomRef.top,
                    bottom = emptyBottomRef.bottom,
                    topMargin = 24.dp,
                    bottomMargin = 16.dp,
                    bias = 1f
                )
                start.linkTo(emptyBottomRef.start, 16.dp)
                end.linkTo(emptyBottomRef.end, 16.dp)
                width = Dimension.fillToConstraints
            }
        ) {
            Text(text = "See locations")
        }
    }
}

private fun getSecondTypeVisibility(hasSecondType: Boolean) =
    if (hasSecondType) Visibility.Visible else Visibility.Gone

@Composable
private fun TypeLayout(type: String?, modifier: Modifier) {
    Text(
        text = type?.capitalizeWords().orEmpty(),
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
                top.linkTo(infoRef.bottom, 4.dp)
                centerHorizontallyTo(infoRef)
                width = Dimension.fillToConstraints
            }
        )
    }
}

@Composable
private fun StatsLayout(stats: List<Stat>?, mainColor: Color, modifier: Modifier) {
    ConstraintLayout(modifier = modifier) {
        val (titleRef, listRef) = createRefs()

        Text(
            text = "Base stats",
            style = AppTypography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.constrainAs(titleRef) {
                top.linkTo(parent.top, 16.dp)
                centerHorizontallyTo(parent)
                width = Dimension.fillToConstraints
            }
        )

        LazyColumn(
            userScrollEnabled = false,
            modifier = Modifier
                .heightIn(max = 5_000.dp)
                .constrainAs(listRef) {
                    top.linkTo(titleRef.bottom, 16.dp)
                    centerHorizontallyTo(titleRef)
                    width = Dimension.fillToConstraints
                }
        ) {
            items(items = stats.orEmpty()) {
                val name = when (it.name) {
                    "special-attack" -> "sp.-atk"
                    "special-defense" -> "sp.-def"
                    else -> it.name
                }
                StatItem(name.orEmpty(), it.value, mainColor, Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun StatItem(name: String, value: Int?, color: Color, modifier: Modifier) {
    ConstraintLayout(modifier = modifier) {
        val (nameRef, valueRef, progressRef) = createRefs()
        val statsNameGuideline = createGuidelineFromStart(0.2f)

        Text(
            text = name.let { if (it.length > 2) it.capitalizeWords() else it.uppercase() },
            style = AppTypography.titleSmall.copy(
                color = color,
                textAlign = TextAlign.End
            ),
            modifier = Modifier.constrainAs(nameRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(statsNameGuideline)
                width = Dimension.fillToConstraints
            }
        )

        Text(
            text = value?.toString().orEmpty().padStart(3, '0'),
            style = AppTypography.bodyMedium,
            modifier = Modifier.constrainAs(valueRef) {
                centerVerticallyTo(nameRef)
                start.linkTo(nameRef.end, 16.dp)
                end.linkTo(progressRef.start)
                width = Dimension.wrapContent
            }
        )

        LinearProgressIndicator(
            progress = { (value?.toFloat()?.div(100f)) ?: 0f },
            color = color,
            trackColor = Color(0xFFa5a5a5),
            drawStopIndicator = {},
            modifier = Modifier.constrainAs(progressRef) {
                centerVerticallyTo(valueRef)
                start.linkTo(valueRef.end, 16.dp)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        )
    }
}
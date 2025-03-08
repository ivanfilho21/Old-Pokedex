package br.iwan.oldpokedex.ui.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import br.iwan.oldpokedex.R
import br.iwan.oldpokedex.data.local.entity.PokemonEntity
import br.iwan.oldpokedex.ui.view_model.DetailsLayoutViewModel

@Preview
@Composable
private fun Preview() {
    DefaultPreview {
        PokemonDetailsScreenContent(
            viewModel = viewModel<DetailsLayoutViewModel>().apply {
                pokemonData = PokemonEntity(0, "Pokémon name", "Description")
            }
        )
    }
}

@Composable
fun PokemonDetailsScreenContent(viewModel: DetailsLayoutViewModel) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (bgRef, imgRef, nameRef, descRef) = createRefs()

        val pokemonData = viewModel.pokemonData

        val mainContentGuidelineStart = createGuidelineFromStart(16.dp)
        val mainContentGuidelineEnd = createGuidelineFromEnd(16.dp)

        val radius = 32.dp
        
        Box(
            modifier = Modifier
                .background(
                    Color(0xFF333333),
                    RoundedCornerShape(bottomStart = radius, bottomEnd = radius)
                )
                .constrainAs(bgRef) {
                    top.linkTo(parent.top)
                    centerHorizontallyTo(parent)
                    width = Dimension.fillToConstraints
                    height = 200.dp.asDimension()
                }
        )

        Image(
            painter = painterResource(R.drawable.ic_launcher_background),
            contentDescription = "Pokémon image",
            modifier = Modifier.constrainAs(imgRef) {
                top.linkTo(bgRef.bottom, (-80).dp)
                centerHorizontallyTo(parent)
                width = Dimension.fillToConstraints
            }
        )

        Text(
            text = "${pokemonData?.id} - ${pokemonData?.name}",
            modifier = Modifier.constrainAs(nameRef) {
                top.linkTo(imgRef.bottom, 16.dp)
                start.linkTo(mainContentGuidelineStart)
                end.linkTo(mainContentGuidelineEnd)
                width = Dimension.fillToConstraints
            }
        )

        Text(
            text = pokemonData?.description.orEmpty(),
            modifier = Modifier.constrainAs(descRef) {
                top.linkTo(nameRef.bottom, 16.dp)
                centerHorizontallyTo(nameRef)
                width = Dimension.fillToConstraints
            }
        )
    }
}
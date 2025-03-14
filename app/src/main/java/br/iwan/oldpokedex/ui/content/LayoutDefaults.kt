package br.iwan.oldpokedex.ui.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.iwan.oldpokedex.BuildConfig
import br.iwan.oldpokedex.ui.theme.AppTypography
import br.iwan.oldpokedex.ui.theme.PokeDexTheme
import br.iwan.oldpokedex.ui.theme.backgroundColor

@Composable
fun DefaultPreview(content: @Composable BoxScope.() -> Unit) {
    PokeDexTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor),
            content = content
        )
    }
}

@Composable
fun MainLayout(
    isLoading: Boolean,
    error: String?,
    onTryAgainClick: () -> Unit,
    modifier: Modifier? = null,
    mainContent: @Composable () -> Unit
) {
    ConstraintLayout(modifier = modifier ?: Modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.constrainAs(createRef()) {
                    centerTo(parent)
                }
            )
        } else {
            error?.let {
                ErrorLayout(
                    debugMessage = it,
                    onTryAgainClick = onTryAgainClick,
                    modifier = Modifier.constrainAs(createRef()) {
                        centerTo(parent)
                        width = Dimension.fillToConstraints
                    }
                )
            }
        } ?: run {
            mainContent()
        }
    }
}

@Composable
fun ErrorLayout(
    debugMessage: String? = null,
    onTryAgainClick: () -> Unit,
    modifier: Modifier
) {
    ConstraintLayout(modifier = modifier.padding(16.dp)) {
        val (msgRef, btnRef) = createRefs()

        Text(
            text = debugMessage?.let { if (BuildConfig.DEBUG) it else null } ?: "There was an error while loading this page.",
            style = AppTypography.bodyMedium.copy(
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.constrainAs(msgRef) {
                top.linkTo(parent.top)
                centerHorizontallyTo(parent)
                width = Dimension.fillToConstraints
            }
        )

        Button(
            onClick = onTryAgainClick,
            modifier = Modifier.constrainAs(btnRef) {
                top.linkTo(msgRef.bottom, 16.dp)
                bottom.linkTo(parent.bottom)
                centerHorizontallyTo(msgRef)
                width = Dimension.wrapContent
            }
        ) {
            Text(
                text = "Try again",
                style = AppTypography.bodyMedium
            )
        }
    }
}
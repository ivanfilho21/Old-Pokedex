package br.iwan.oldpokedex.ui.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
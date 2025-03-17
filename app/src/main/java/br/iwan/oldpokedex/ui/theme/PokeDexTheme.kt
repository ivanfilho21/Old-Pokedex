package br.iwan.oldpokedex.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun PokeDexTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            primary = primaryColor,
            primaryContainer = primaryColor,
            surface = primaryColor,
            background = backgroundColor
        ),
        content = content
    )
}
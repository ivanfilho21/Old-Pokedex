package br.iwan.oldpokedex.ui.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import br.iwan.oldpokedex.data.local.entity.PokemonEntity
import br.iwan.oldpokedex.ui.view_model.HomeLayoutViewModel
import kotlinx.coroutines.delay

@Preview
@Composable
private fun Preview() {
    DefaultPreview {
        HomeScreenContent(
            viewModel = viewModel<HomeLayoutViewModel>().apply {
                searchBarExpanded = false

                pokemonList.addAll(
                    listOf(
                        "Bulbasaur",
                        "Ivysaur",
                        "Venosaur",
                        "Charmander",
                        "Charmeleon",
                        "Charizard",
                        "Squirtle",
                        "Wartortle",
                        "Blastoise"
                    ).mapIndexed { i, it ->
                        PokemonEntity(i + 1, it)
                    }
                )
            },
            onSearch = {},
            onPokemonClick = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    viewModel: HomeLayoutViewModel,
    onSearch: (String) -> Unit,
    onPokemonClick: (String) -> Unit
) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (searchBarRef, listRef) = createRefs()

        // Barra de pesquisa para filtrar por nome

        LaunchedEffect(viewModel.searchBarQuery) {
            if (viewModel.searchBarQuery.isBlank()) return@LaunchedEffect

            viewModel.clearSuggestions()

            delay(1000)

            if (viewModel.searchBarQuery.length >= 3) {
                onSearch(viewModel.searchBarQuery)
            }
        }

        var suggestionItemsEnabled by remember {
            mutableStateOf(true)
        }

        LaunchedEffect(suggestionItemsEnabled) {
            if (suggestionItemsEnabled) return@LaunchedEffect

            delay(500)

            suggestionItemsEnabled = true
        }

        val suggestions = viewModel.suggestions

        DockedSearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = viewModel.searchBarQuery,
                    onQueryChange = { query ->
                        if (viewModel.searchBarQuery != query) {
                            viewModel.searchBarQuery = query
                        }
                    },
                    onSearch = { text ->
                        viewModel.onSearchBarAction(text, onPokemonClick)
                    },
                    expanded = suggestions.isNotEmpty(),
                    onExpandedChange = { _ -> },
                    placeholder = { Text("Digite o nome") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = null,
                            modifier = Modifier.clickable(
                                enabled = suggestionItemsEnabled,
                                onClick = {
                                    // mostrar opções
                                }
                            )
                        )
                    },
                )
            },
            expanded = suggestions.isNotEmpty(),
            onExpandedChange = { _ -> },
            modifier = Modifier.constrainAs(searchBarRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(items = suggestions) { item ->
                    ListItem(
                        headlineContent = {
                            Text(text = item)
                        },
                        modifier = Modifier.clickable(
                            enabled = suggestionItemsEnabled,
                            onClick = {
                                viewModel.onSearchBarAction(item, onPokemonClick)
                            }
                        )
                    )
                }
            }
        }

        // menu para filtrar por tipo, etc.
        // ordenar por id, altura, peso, etc.

        // Listagem
        val pokemonList = viewModel.pokemonList

        LazyColumn(
            modifier = Modifier.constrainAs(listRef) {
                top.linkTo(searchBarRef.bottom, 24.dp)
                centerHorizontallyTo(searchBarRef)
                width = Dimension.fillToConstraints
            }
        ) {
            items(items = pokemonList) { item ->
                ListItem(
                    headlineContent = {
                        Text(text = item.name.orEmpty())
                    },
                    leadingContent = {
                        Text(
                            text = "${item.id}"
                        )
                    },
                    modifier = Modifier.clickable(
                        enabled = suggestionItemsEnabled,
                        onClick = {
                            onPokemonClick(item.name.orEmpty())
                        }
                    )
                )
            }
        }
    }
}
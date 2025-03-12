@file:OptIn(ExperimentalMaterial3Api::class)

package br.iwan.oldpokedex.ui.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
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
import java.util.UUID

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
                        PokemonEntity(UUID.randomUUID(), i + 1, it)
                    }
                )
            },
            onSearch = {},
            onPokemonClick = {},
            onTryAgainClick = {}
        )
    }
}

@Composable
fun HomeScreenContent(
    viewModel: HomeLayoutViewModel,
    onSearch: (String) -> Unit,
    onPokemonClick: (Int) -> Unit,
    onTryAgainClick: () -> Unit
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (searchBarRef, listRef) = createRefs()

        // Barra de pesquisa para filtrar por nome

        LaunchedEffect(viewModel.searchBarQuery) {
            if (viewModel.searchBarQuery.isBlank()) return@LaunchedEffect

            viewModel.clearSuggestions()

            delay(100)

            if (viewModel.searchBarQuery.length >= 3) {
                onSearch(viewModel.searchBarQuery)
            }
        }

        SearchBarContent(
            suggestions = viewModel.suggestions,
            query = viewModel.searchBarQuery,
            onQueryChange = { query ->
                if (viewModel.searchBarQuery != query) {
                    viewModel.searchBarQuery = query
                }
            },
            modifier = Modifier.constrainAs(searchBarRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        )

        if (viewModel.loading) {
            CircularProgressIndicator(
                modifier = Modifier.constrainAs(createRef()) {
                    centerHorizontallyTo(searchBarRef)
                    top.linkTo(searchBarRef.bottom)
                    bottom.linkTo(parent.bottom)
                }
            )
        } else {
            viewModel.error?.let {
                ErrorLayout(
                    debugMessage = it,
                    onTryAgainClick = onTryAgainClick,
                    modifier = Modifier.constrainAs(listRef) {
                        top.linkTo(searchBarRef.bottom, 24.dp)
                        bottom.linkTo(parent.bottom, 16.dp)
                        centerHorizontallyTo(searchBarRef)
                        width = Dimension.fillToConstraints
                    }
                )
            } ?: run {
                MainContent(
                    pokemonList = viewModel.pokemonList,
                    onPokemonClick = onPokemonClick,
                    modifier = Modifier.constrainAs(listRef) {
                        top.linkTo(searchBarRef.bottom)
                        bottom.linkTo(parent.bottom)
                        centerHorizontallyTo(searchBarRef)

                        Dimension.fillToConstraints.let {
                            width = it
                            height = it
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun SearchBarContent(
    suggestions: List<String>,
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier
) {
    // menu para filtrar por tipo, etc.
    // ordenar por id, altura, peso, etc.

    var suggestionItemsEnabled by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(suggestionItemsEnabled) {
        if (suggestionItemsEnabled) return@LaunchedEffect

        delay(500)

        suggestionItemsEnabled = true
    }

    DockedSearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = { _ ->
                    //
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
        modifier = modifier
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
                            //
                        }
                    )
                )
            }
        }
    }
}

@Composable
private fun MainContent(
    pokemonList: List<PokemonEntity>,
    onPokemonClick: (Int) -> Unit,
    modifier: Modifier
) {
    var itemEnabled by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(itemEnabled) {
        if (itemEnabled) return@LaunchedEffect

        delay(500)

        itemEnabled = true
    }

    LazyColumn(modifier = modifier) {
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
                    enabled = itemEnabled,
                    onClick = {
                        onPokemonClick(item.id)
                    }
                )
            )
        }
    }
}
package br.iwan.oldpokedex.ui.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import br.iwan.oldpokedex.ui.theme.backgroundColor
import br.iwan.oldpokedex.ui.view_model.HomeViewModel
import kotlinx.coroutines.delay
import kotlin.random.Random

@Preview
@Composable
private fun Preview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        HomeScreenContent(
            viewModel = viewModel<HomeViewModel>().apply {
                searchBarExpanded = false

                pokemonListSF.value.toMutableList().addAll(
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
                        PokemonEntity(i + 1, it, "")
                    }
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(viewModel: HomeViewModel) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (searchBarRef, listRef) = createRefs()

        val suggestions = remember {
            mutableStateListOf<String>()
        }
        // Barra de pesquisa para filtrar por nome

        LaunchedEffect(viewModel.searchBarQuery) {
            if (viewModel.searchBarQuery.isBlank()) return@LaunchedEffect

            delay(1000)

            if (suggestions.size < 3)
                suggestions.add("Pokémon " + Random.nextInt(152))
        }

        var suggestionItemsEnabled by remember {
            mutableStateOf(true)
        }

        LaunchedEffect(suggestionItemsEnabled) {
            if (suggestionItemsEnabled) return@LaunchedEffect

            delay(500)

            suggestionItemsEnabled = true
        }

        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = viewModel.searchBarQuery,
                    onQueryChange = { query ->
                        viewModel.searchBarQuery = query
                    },
                    onSearch = { viewModel.searchBarExpanded = false },
                    expanded = viewModel.searchBarExpanded,
                    onExpandedChange = { viewModel.searchBarExpanded = it },
                    placeholder = { Text("Digite o nome") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = null,
                            modifier = Modifier.clickable(
                                enabled = suggestionItemsEnabled,
                                onClick = {
                                    //
                                }
                            )
                        )
                    },
                )
            },
            expanded = viewModel.searchBarExpanded,
            onExpandedChange = { expanded ->
                if (expanded) {
                    viewModel.searchBarQuery = ""
                    suggestions.clear()
                }
                viewModel.searchBarExpanded = expanded
            },
            modifier = Modifier.constrainAs(searchBarRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        ) {
            // Ir adicionando numa lista e depois mostrar no LazyColumn

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(items = suggestions) { item ->
                    ListItem(
                        headlineContent = {
                            Text(text = item)
                        },
                        modifier = Modifier.clickable(
                            enabled = suggestionItemsEnabled,
                            onClick = {
                                // same action as searching
                            }
                        )
                    )
                }
            }
        }

        // menu para filtrar por tipo, etc.
        // ordenar por id, altura, peso, etc.

        // Listagem
        val pokemonList by viewModel.pokemonListSF.collectAsState()

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
                            // open details screen
                        }
                    )
                )
            }
        }
    }
}
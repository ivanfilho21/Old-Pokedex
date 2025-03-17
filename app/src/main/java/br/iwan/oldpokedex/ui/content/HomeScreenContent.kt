@file:OptIn(ExperimentalMaterial3Api::class)

package br.iwan.oldpokedex.ui.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
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
import br.iwan.oldpokedex.ui.helper.PokemonHelper.formatPokemonName
import br.iwan.oldpokedex.ui.theme.backgroundColor
import br.iwan.oldpokedex.ui.view_model.HomeLayoutViewModel
import kotlinx.coroutines.delay
import java.util.UUID

val previewList = listOf(
    "bulbasaur",
    "ivysaur",
    "venosaur",
    "charmander",
    "charmeleon",
    "charizard",
    "squirtle",
    "nidoran-m",
    "nidoran-f",
    "wartortle",
    "blastoise"
).mapIndexed { i, it ->
    PokemonEntity(UUID.randomUUID(), i + 1, it)
}

@Preview
@Composable
private fun Preview() {
    DefaultPreview {
        HomeScreenContent(
            viewModel = viewModel<HomeLayoutViewModel>().apply {
                updatePokemonList(previewList)
            },
            onPokemonClick = {},
            onTryAgainClick = {}
        )
    }
}

@Preview
@Composable
private fun PreviewFiltered() {
    DefaultPreview {
        HomeScreenContent(
            viewModel = viewModel<HomeLayoutViewModel>().apply {
                updatePokemonList(previewList)

                searchBarQuery = "ran"
                updateFilter()
            },
            onPokemonClick = {},
            onTryAgainClick = {}
        )
    }
}

@Composable
fun HomeScreenContent(
    viewModel: HomeLayoutViewModel,
    onPokemonClick: (Int) -> Unit,
    onTryAgainClick: () -> Unit
) {
    MainLayout(
        isLoading = viewModel.loading,
        error = viewModel.error,
        onTryAgainClick = onTryAgainClick
    ) {
        Content(
            viewModel = viewModel,
            onPokemonClick = onPokemonClick
        )
    }
}

@Composable
private fun SearchBarContent(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier
) {
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
                expanded = false,
                onExpandedChange = { _ -> },
                placeholder = {
                    Text("Pokémon name")
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null
                    )
                },
                trailingIcon = {}
            )
        },
        expanded = false,
        onExpandedChange = { _ -> },
        modifier = modifier
    ) {}
}

@Composable
private fun Content(
    viewModel: HomeLayoutViewModel,
    onPokemonClick: (Int) -> Unit
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (searchBarRef, listRef) = createRefs()

        LaunchedEffect(viewModel.searchBarQuery) {
            delay(10)
            viewModel.updateFilter()
        }

        SearchBarContent(
            query = viewModel.searchBarQuery,
            onQueryChange = { query ->
                if (viewModel.searchBarQuery != query) {
                    viewModel.searchBarQuery = query
                }
            },
            modifier = Modifier.constrainAs(searchBarRef) {
                top.linkTo(parent.top, 16.dp)
                start.linkTo(parent.start, 16.dp)
                end.linkTo(parent.end, 16.dp)
                width = Dimension.fillToConstraints
            }
        )

        var itemEnabled by remember {
            mutableStateOf(true)
        }

        LaunchedEffect(itemEnabled) {
            if (itemEnabled) return@LaunchedEffect
            delay(500)
            itemEnabled = true
        }

        LazyColumn(
            modifier = Modifier.constrainAs(listRef) {
                top.linkTo(searchBarRef.bottom)
                bottom.linkTo(parent.bottom)
                centerHorizontallyTo(parent)

                Dimension.fillToConstraints.let {
                    width = it
                    height = it
                }
            }
        ) {
            items(items = viewModel.pokemonList) { item ->
                ListItem(
                    headlineContent = {
                        Text(text = item.name?.formatPokemonName().orEmpty())
                    },
                    leadingContent = {
                        Text(
                            text = "${item.id}"
                        )
                    },
                    colors = ListItemDefaults.colors(containerColor = backgroundColor),
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
}
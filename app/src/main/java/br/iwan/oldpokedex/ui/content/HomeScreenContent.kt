package br.iwan.oldpokedex.ui.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import br.iwan.oldpokedex.ui.theme.backgroundColor
import br.iwan.oldpokedex.ui.view_model.HomeViewModel

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
                searchBarExpanded = true
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(viewModel: HomeViewModel) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (searchBarRef, titleRef) = createRefs()

        // Barra de pesquisa para filtrar por nome

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
                    trailingIcon = { Icon(Icons.Default.MoreVert, contentDescription = null) },
                )
            },
            expanded = viewModel.searchBarExpanded,
            onExpandedChange = { _ -> },
            modifier = Modifier.constrainAs(searchBarRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        ) {
            // Ir adicionando numa lista e depois mostrar no LazyColumn

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                item {
                    ListItem(
                        headlineContent = {
                            Text(text = "Pokémon 1")
                        }
                    )
                }
            }
        }

        // menu para filtrar por tipo, etc.
        // ordenar por id, altura, peso, etc.

        Text(
            text = "Título",
            modifier = Modifier.constrainAs(titleRef) {
                top.linkTo(searchBarRef.bottom, 24.dp)
                centerHorizontallyTo(searchBarRef)
                width = Dimension.fillToConstraints
            }
        )

        // Listagem
    }
}
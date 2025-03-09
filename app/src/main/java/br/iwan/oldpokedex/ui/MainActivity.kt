package br.iwan.oldpokedex.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.iwan.oldpokedex.ui.content.HomeScreenContent
import br.iwan.oldpokedex.ui.content.PokemonDetailsScreenContent
import br.iwan.oldpokedex.ui.content.PokemonLocationsScreenContent
import br.iwan.oldpokedex.ui.navigation.HomeScreen
import br.iwan.oldpokedex.ui.navigation.PokemonDetailsScreen
import br.iwan.oldpokedex.ui.navigation.PokemonLocationsScreen
import br.iwan.oldpokedex.ui.theme.PokeDexTheme
import br.iwan.oldpokedex.ui.theme.backgroundColor
import br.iwan.oldpokedex.ui.view_model.DetailsLayoutViewModel
import br.iwan.oldpokedex.ui.view_model.DetailsViewModel
import br.iwan.oldpokedex.ui.view_model.HomeLayoutViewModel
import br.iwan.oldpokedex.ui.view_model.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val homeVM by viewModels<HomeViewModel>()
    private val detailsVM by viewModels<DetailsViewModel>()
    private lateinit var homeLVM: HomeLayoutViewModel
    private lateinit var detailsLVM: DetailsLayoutViewModel
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            navController = rememberNavController()

            homeLVM = viewModel()
            detailsLVM = viewModel()

            observers()

            PokeDexTheme {
                ScreenContent()
            }
        }
    }

    private fun observers() {
        observePokemonList()
        observeSuggestions()
        observePokemonData()
    }

    private fun observePokemonList() {
        lifecycleScope.launch {
            homeVM.pokemonListSF.collect { result ->
                homeLVM.updatePokemonList(result)
            }
        }
    }

    private fun observeSuggestions() {
        lifecycleScope.launch {
            homeVM.suggestionsSF.collect { result ->
                homeLVM.updateSuggestions(result)
            }
        }
    }

    private fun observePokemonData() {
        lifecycleScope.launch {
            detailsVM.pokemonDataSF.collect { result ->
                detailsLVM.pokemonData = result
            }
        }
    }

    private infix fun <T : Any> String?.has(value: T) =
        this?.contains(value::class.qualifiedName.orEmpty().replace(".Companion", "")) ?: false

    @Preview
    @Composable
    private fun Preview() {
        navController = rememberNavController()

        homeLVM = viewModel()
        detailsLVM = viewModel()

        PokeDexTheme {
            ScreenContent()
        }
    }

    @Composable
    private fun ScreenContent() {
        Scaffold(
            containerColor = backgroundColor,
            content = {
                Navigation(innerPadding = it)
            }
        )
    }

    @Composable
    private fun Navigation(innerPadding: PaddingValues) {
        val currentBackStackEntry by navController.currentBackStackEntryAsState()

        LaunchedEffect(currentBackStackEntry) {
            currentBackStackEntry?.let { bse ->
                val destination = bse.destination
                val args = bse.arguments

                destination.route?.let {
                    when {
                        it has HomeScreen -> homeVM.listAllPokemon()

                        it has PokemonDetailsScreen ->
                            args?.getInt("id")?.let { id ->
                                detailsVM.findById(id)
                            }

                        it has PokemonLocationsScreen ->
                            args?.getInt("id")?.let { _ ->
                                // call API and then store in db
                            }
                    }
                }
            }
        }

        NavHost(
            navController = navController,
            startDestination = HomeScreen,
            modifier = Modifier.padding(innerPadding),
            builder = {
                composable<HomeScreen> {
                    HomeScreenContent(
                        viewModel = homeLVM,
                        onSearch = { name ->
                            homeVM.searchByName(name)
                        },
                        onPokemonClick = { pokemon ->
                            navController.navigate(PokemonDetailsScreen(id = pokemon))
                        }
                    )
                }

                composable<PokemonDetailsScreen> {
                    PokemonDetailsScreenContent(
                        viewModel = detailsLVM,
                        seeLocationsClick = { pokemon ->
                            navController.navigate(PokemonLocationsScreen(id = pokemon))
                        }
                    )
                }

                composable<PokemonLocationsScreen> {
                    PokemonLocationsScreenContent()
                }
            }
        )
    }
}
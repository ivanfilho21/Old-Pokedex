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
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.iwan.oldpokedex.ui.content.HomeScreenContent
import br.iwan.oldpokedex.ui.content.PokemonDetailsScreenContent
import br.iwan.oldpokedex.ui.navigation.HomeScreen
import br.iwan.oldpokedex.ui.navigation.PokemonDetailsScreen
import br.iwan.oldpokedex.ui.theme.PokeDexTheme
import br.iwan.oldpokedex.ui.theme.backgroundColor
import br.iwan.oldpokedex.ui.view_model.DetailsViewModel
import br.iwan.oldpokedex.ui.view_model.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val homeVM by viewModels<HomeViewModel>()
    private val detailsVM by viewModels<DetailsViewModel>()
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            navController = rememberNavController().apply {
                addOnDestinationChangedListener { controller, destination, _ ->

                    when (controller.currentDestination?.label) {
                        PokemonDetailsScreen.toString() -> {}
                    }
                }
            }

            homeVM.listAllPokemon()

            PokeDexTheme {
                ScreenContent()
            }
        }
    }

    @Preview
    @Composable
    private fun Preview() {
        navController = rememberNavController()

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
                    if (it.contains(PokemonDetailsScreen::class.java.simpleName)) {
                        args?.getString("pokemonName")?.let { pokemonName ->
                            detailsVM.findByName(pokemonName)
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
                        viewModel = homeVM,
                        onPokemonClick = { pokemon ->
                            navController.navigate(PokemonDetailsScreen(pokemonName = pokemon))
                        }
                    )
                }

                composable<PokemonDetailsScreen> {
                    PokemonDetailsScreenContent(viewModel = detailsVM)
                }
            }
        )
    }
}
package br.iwan.oldpokedex.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.iwan.oldpokedex.data.model.UiResponse
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
import br.iwan.oldpokedex.ui.view_model.LocationsLayoutViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {
    private val homeVM by viewModels<HomeViewModel>()
    private val detailsVM by viewModels<DetailsViewModel>()
    private lateinit var homeLVM: HomeLayoutViewModel
    private lateinit var detailsLVM: DetailsLayoutViewModel
    private lateinit var locationsLVM: LocationsLayoutViewModel
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            navController = rememberNavController().apply {
                addOnDestinationChangedListener(this@MainActivity)
            }

            homeLVM = viewModel()
            detailsLVM = viewModel()
            locationsLVM = viewModel()

            observers()

            PokeDexTheme {
                ScreenContent()
            }
        }
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        destination.route?.let {
            when {
                it has HomeScreen -> homeVM.listAllPokemon()

                it has PokemonDetailsScreen ->
                    arguments?.getInt("id")?.let { id ->
                        detailsVM.findById(id)
                    }

                it has PokemonLocationsScreen ->
                    arguments?.getInt("id")?.let { _ ->
                        // call API and then store in db
                    }

                else -> {
                    // nothing yet
                }
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
                when (result) {
                    is UiResponse.Success -> homeLVM.updatePokemonList(result.data)

                    is UiResponse.Error -> homeLVM.error = result.message

                    is UiResponse.Loading -> homeLVM.loading = true
                }
            }
        }
    }

    private fun observeSuggestions() {
        lifecycleScope.launch {
            homeVM.suggestionsSF.collect { result ->
                when (result) {
                    is UiResponse.Success -> homeLVM.updateSuggestions(result.data)

                    else -> {
                        // nothing yet
                    }
                }
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
        locationsLVM = viewModel()

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
                        },
                        onTryAgainClick = {
                            homeVM.listAllPokemon()
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
                    PokemonLocationsScreenContent(
                        viewModel = locationsLVM
                    )
                }
            }
        )
    }
}
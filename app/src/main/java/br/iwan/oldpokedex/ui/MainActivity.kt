package br.iwan.oldpokedex.ui

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.iwan.oldpokedex.R
import br.iwan.oldpokedex.data.model.UiResponse
import br.iwan.oldpokedex.ui.content.HomeScreenContent
import br.iwan.oldpokedex.ui.content.PokemonDetailsScreenContent
import br.iwan.oldpokedex.ui.content.PokemonLocationsScreenContent
import br.iwan.oldpokedex.ui.helper.ColorHelper
import br.iwan.oldpokedex.ui.helper.PokemonHelper.formatPokemonName
import br.iwan.oldpokedex.ui.navigation.HomeScreen
import br.iwan.oldpokedex.ui.navigation.PokemonDetailsScreen
import br.iwan.oldpokedex.ui.navigation.PokemonLocationsScreen
import br.iwan.oldpokedex.ui.theme.PokeDexTheme
import br.iwan.oldpokedex.ui.theme.primaryColor
import br.iwan.oldpokedex.ui.theme.primaryColorDark
import br.iwan.oldpokedex.ui.view_model.DetailsLayoutViewModel
import br.iwan.oldpokedex.ui.view_model.DetailsViewModel
import br.iwan.oldpokedex.ui.view_model.HomeLayoutViewModel
import br.iwan.oldpokedex.ui.view_model.HomeViewModel
import br.iwan.oldpokedex.ui.view_model.LocationsLayoutViewModel
import br.iwan.oldpokedex.ui.view_model.LocationsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {
    private val homeVM by viewModels<HomeViewModel>()
    private val detailsVM by viewModels<DetailsViewModel>()
    private val locationsVM by viewModels<LocationsViewModel>()
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
        supportActionBar?.run {
            show()
            setDisplayHomeAsUpEnabled(true)
        }

        destination.route?.let {
            when {
                it has HomeScreen -> {
                    listPokemon()
                    supportActionBar?.run {
                        title = applicationInfo.loadLabel(packageManager)
                        setDisplayHomeAsUpEnabled(false)
                    }
                    changeAppBarColor(primaryColor, primaryColorDark)
                }

                it has PokemonDetailsScreen -> {
                    detailsLVM.currentId = arguments?.getInt("id")

                    supportActionBar?.title = homeLVM.pokemonList.find { p ->
                        p.id == detailsLVM.currentId
                    }?.name?.formatPokemonName().orEmpty()

                    getPokemonDetails()
                }

                it has PokemonLocationsScreen -> {
                    locationsLVM.currentId = arguments?.getInt("id")
                    getPokemonLocations()

                    supportActionBar?.run {
                        title = getString(R.string.locations)
                    }
                }

                else -> {
                    // empty
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun changeAppBarColor(actionBarColor: Color, statusBarColor: Color? = null) {
        val secondColor = statusBarColor ?: actionBarColor

        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(actionBarColor.toArgb())
        )

        window.statusBarColor = secondColor.toArgb()
    }

    private fun observers() {
        observePokemonList()
        observePokemonDetails()
        observePokemonLocations()
    }

    private fun observePokemonList() {
        lifecycleScope.launch {
            homeVM.pokemonListSF.collect {
                homeLVM.loading = false

                if (it is UiResponse.Success)
                    homeLVM.updatePokemonList(it.data)

                if (it is UiResponse.Error)
                    homeLVM.error = it.message
            }
        }
    }

    private fun observePokemonDetails() {
        lifecycleScope.launch {
            detailsVM.pokemonDataSF.collect {
                detailsLVM.loading = false

                if (it is UiResponse.Success) {
                    detailsLVM.pokemonData = it.data
                    changeAppBarColor(
                        ColorHelper.getColorByPokemonType(detailsLVM.pokemonData?.type1)
                    )
                }

                if (it is UiResponse.Error)
                    detailsLVM.error = it.message
            }
        }
    }

    private fun observePokemonLocations() {
        lifecycleScope.launch {
            locationsVM.locationsSF.collect {
                locationsLVM.loading = false

                if (it is UiResponse.Success)
                    locationsLVM.run {
                        locationData = it.data
                        pokemonName = detailsLVM.pokemonData?.name
                    }

                if (it is UiResponse.Error)
                    locationsLVM.error = it.message
            }
        }
    }

    private fun listPokemon() {
        homeLVM.run {
            if (pokemonList.isEmpty()) {
                loading = true
                error = null
                homeVM.listAllPokemon()
            }
        }
    }

    private fun getPokemonDetails() {
        detailsLVM.run {
            currentId?.let { id ->
                if (pokemonData?.id != id) {
                    loading = true
                    error = null
                    detailsVM.getDetails(id)
                }
            }
        }
    }

    private fun getPokemonLocations() {
        locationsLVM.run {
            currentId?.let { id ->
                if (locationData?.pokemonId != id) {
                    loading = true
                    error = null
                    locationsVM.getLocationsByPokemonId(id)
                }
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
                        onPokemonClick = { pokemon ->
                            detailsLVM.currentId = pokemon
                            navController.navigate(PokemonDetailsScreen(id = pokemon))
                        },
                        onFavoriteClick = homeVM::favoritePokemon,
                        onTryAgainClick = ::listPokemon
                    )
                }

                composable<PokemonDetailsScreen> {
                    PokemonDetailsScreenContent(
                        viewModel = detailsLVM,
                        seeLocationsClick = { pokemon ->
                            navController.navigate(PokemonLocationsScreen(id = pokemon))
                        },
                        playCryClick = detailsVM::playPokemonCry,
                        onFavoriteClick = { id, favorite ->
                            lifecycleScope.launch {
                                homeVM.favoritePokemon(id, favorite)
                                delay(100L)
                                detailsVM.getDetails(id)
                            }
                        },
                        onTryAgainClick = ::getPokemonDetails
                    )
                }

                composable<PokemonLocationsScreen> {
                    PokemonLocationsScreenContent(
                        viewModel = locationsLVM,
                        onTryAgainClick = ::getPokemonLocations
                    )
                }
            }
        )
    }
}
package br.iwan.oldpokedex.data.use_case

import br.iwan.oldpokedex.data.repository.LocationRepository
import br.iwan.oldpokedex.data.repository.PokemonRepository
import javax.inject.Inject

class PokemonUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository,
    private val locationRepository: LocationRepository
) {
    suspend fun listAll() = pokemonRepository.listAll()

    suspend fun findById(id: Int) = pokemonRepository.getDetails(id)

    suspend fun findAllLocationsByPokemonId(id: Int) =
        locationRepository.findByPokemonId(id)

    suspend fun favoritePokemon(id: Int, favorite: Boolean) =
        pokemonRepository.favorite(id, favorite)
}
package br.iwan.oldpokedex.data.use_case

import br.iwan.oldpokedex.data.repository.PokemonRepository
import javax.inject.Inject

class PokemonUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    suspend fun listAll() = pokemonRepository.listAll()

    suspend fun searchByName(query: String) = pokemonRepository.searchByName(query)

    suspend fun findByName(name: String) = pokemonRepository.findByName(name)
}
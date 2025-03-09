package br.iwan.oldpokedex.data.repository

import br.iwan.oldpokedex.data.local.dao.PokemonLocationDao
import br.iwan.oldpokedex.data.local.entity.PokemonLocationEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val dao: PokemonLocationDao
) {
    suspend fun findByPokemonId(pokemonId: Int): List<PokemonLocationEntity> {
        return withContext(ioDispatcher) {
            dao.findByPokemonId(pokemonId)
        }
    }

    private suspend fun persist(list: List<PokemonLocationEntity>) {
        withContext(ioDispatcher) {
            dao.insertAll(list)
        }
    }
}
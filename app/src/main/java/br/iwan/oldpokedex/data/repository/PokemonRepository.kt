package br.iwan.oldpokedex.data.repository

import br.iwan.oldpokedex.data.local.dao.PokemonDao
import br.iwan.oldpokedex.data.local.entity.PokemonEntity
import br.iwan.oldpokedex.data.remote.PokemonService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PokemonRepository @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val pokemonDao: PokemonDao,
    private val service: PokemonService
) {
    // in the future, decide when to get from remote

    suspend fun listAll(): List<PokemonEntity> {
        return withContext(ioDispatcher) {
            pokemonDao.getAll()
        }
    }

    suspend fun searchByName(name: String): List<PokemonEntity> {
        return withContext(ioDispatcher) {
            pokemonDao.searchByName(name)
        }
    }

    suspend fun findByName(name: String): PokemonEntity {
        return withContext(ioDispatcher) {
            pokemonDao.findByName(name)
        }
    }

    private suspend fun persist(list: List<PokemonEntity>) {
        withContext(ioDispatcher) {
            pokemonDao.insertAll(*list.toTypedArray())
        }
    }
}
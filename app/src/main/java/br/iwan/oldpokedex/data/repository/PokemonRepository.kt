package br.iwan.oldpokedex.data.repository

import br.iwan.oldpokedex.data.local.dao.PokemonDao
import br.iwan.oldpokedex.data.local.entity.PokemonEntity
import br.iwan.oldpokedex.data.model.UiResponse
import br.iwan.oldpokedex.data.remote.PokemonService
import br.iwan.oldpokedex.data.remote.model.ApiRequestResult
import br.iwan.oldpokedex.data.remote.model.PokemonListResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.util.UUID
import javax.inject.Inject

class PokemonRepository @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val pokemonDao: PokemonDao,
    private val service: PokemonService,
    private val json: Json
) {
    suspend fun listAll(): UiResponse<List<PokemonEntity>> {
        return withContext(ioDispatcher) {
            pokemonDao.getAll().let {
                if (it.isEmpty()) listPokemonFromRemote()
                else UiResponse.Success(it)
            }
        }
    }

    private suspend fun listPokemonFromRemote() =
        service.getAllPokemon(151).let { res ->
            when (res) {
                is ApiRequestResult.Success -> {
                    json.decodeFromString<PokemonListResponse>(res.data).results?.map {
                        PokemonEntity(
                            UUID.randomUUID(),
                            it.url?.split("pokemon/")?.lastOrNull()?.trim()?.trim('/')?.toInt()
                                ?: 0,
                            it.name.orEmpty()
                        )
                    }.let { list ->
                        list?.let { l ->
                            persist(l)
                        }

                        UiResponse.Success(list.orEmpty())
                    }
                }

                is ApiRequestResult.Error -> {
                    UiResponse.Error(res.throwable.message.orEmpty())
                }
            }
        }

    suspend fun searchByName(name: String): List<PokemonEntity> {
        return withContext(ioDispatcher) {
            pokemonDao.searchByName(name)
        }
    }

    suspend fun findById(id: Int): PokemonEntity {
        return withContext(ioDispatcher) {
            pokemonDao.findById(id)
        }
    }

    private suspend fun persist(list: List<PokemonEntity>) {
        withContext(ioDispatcher) {
            pokemonDao.insertAll(*list.toTypedArray())
        }
    }
}
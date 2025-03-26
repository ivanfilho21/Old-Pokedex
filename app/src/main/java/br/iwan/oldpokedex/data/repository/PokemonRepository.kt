package br.iwan.oldpokedex.data.repository

import br.iwan.oldpokedex.data.local.dao.PokemonDao
import br.iwan.oldpokedex.data.local.entity.PokemonEntity
import br.iwan.oldpokedex.data.local.entity.Stat
import br.iwan.oldpokedex.data.model.UiResponse
import br.iwan.oldpokedex.data.remote.PokemonService
import br.iwan.oldpokedex.data.remote.model.ApiRequestResult
import br.iwan.oldpokedex.data.remote.model.PokemonListResponse
import br.iwan.oldpokedex.data.remote.model.PokemonResponse
import br.iwan.oldpokedex.data.remote.model.PokemonSpeciesResponse
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
        service.getPokemonList(151).let { res ->
            when (res) {
                is ApiRequestResult.Success -> {
                    json.decodeFromString<PokemonListResponse>(res.data).results?.map {
                        PokemonEntity(
                            uuid = UUID.randomUUID(),
                            id = it.url?.split("pokemon/")
                                ?.lastOrNull()
                                ?.trim()
                                ?.trim('/')
                                ?.toInt()
                                ?: 0,
                            name = it.name.orEmpty()
                        )
                    }.let { list ->
                        list?.let { l ->
                            persist(*l.toTypedArray())
                        }

                        UiResponse.Success(list.orEmpty())
                    }
                }

                is ApiRequestResult.Error ->
                    UiResponse.Error(res.throwable.message.orEmpty())
            }
        }

    suspend fun getDetails(id: Int): UiResponse<PokemonEntity> {
        return withContext(ioDispatcher) {
            pokemonDao.findById(id).let { pokemon ->
                pokemon.description?.let {
                    UiResponse.Success(pokemon)
                } ?: getDetailsFromRemote(pokemon)
            }
        }
    }

    suspend fun favorite(id: Int, favorite: Boolean) = withContext(ioDispatcher) {
        pokemonDao.findById(id).apply {
            this.favorite = favorite
        }.let {
            pokemonDao.update(it)
        }
    }

    private suspend fun getDetailsFromRemote(pokemon: PokemonEntity) =
        service.getPokemonDetails(pokemon.id).let { res ->
            when (res) {
                is ApiRequestResult.Success -> json.decodeFromString<PokemonResponse.Data>(res.data)
                    .let { remotePokemon ->
                        val pokeTypes = remotePokemon.types?.mapNotNull {
                            it.type?.name
                        }.orEmpty()
                        val firstType = pokeTypes.firstOrNull()
                        val secondType = pokeTypes.lastOrNull().let { if (it == firstType) null else it }

                        pokemon.apply {
                            type1 = firstType
                            type2 = secondType
                            height = remotePokemon.height
                            weight = remotePokemon.weight
                            stats = remotePokemon.stats?.map {
                                Stat(it.stat?.name, it.baseStat)
                            }.orEmpty()
                        }.let {
                            update(it)
                            getSpeciesDetailsFromRemote(it)
                        }
                    }

                is ApiRequestResult.Error ->
                    UiResponse.Error(res.throwable.message.orEmpty())
            }
        }

    private suspend fun getSpeciesDetailsFromRemote(pokemon: PokemonEntity) =
        service.getSpeciesDetails(pokemon.id).let { res ->
            when (res) {
                is ApiRequestResult.Success ->
                    json.decodeFromString<PokemonSpeciesResponse.Data>(res.data).let { remoteSpecies ->
                        val pokeDesc = remoteSpecies.descriptions?.filter {
                            it.language?.name?.equals("en", true) == true &&
                                    it.version?.name?.equals("yellow", true) == true
                        }?.firstNotNullOfOrNull {
                            it.text?.trim()?.replace("\n", " ")
                        }

                        pokemon.apply {
                            description = pokeDesc
                        }.let {
                            update(it)
                            UiResponse.Success(it)
                        }
                    }

                is ApiRequestResult.Error -> UiResponse.Error(res.throwable.message.orEmpty())
            }
        }

    private suspend fun persist(vararg pokemon: PokemonEntity) {
        withContext(ioDispatcher) {
            pokemonDao.insertAll(*pokemon)
        }
    }

    private suspend fun update(pokemon: PokemonEntity) {
        withContext(ioDispatcher) {
            pokemonDao.update(pokemon)
        }
    }
}
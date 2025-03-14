package br.iwan.oldpokedex.data.repository

import br.iwan.oldpokedex.data.local.dao.PokemonLocationDao
import br.iwan.oldpokedex.data.local.entity.PokemonLocationEntity
import br.iwan.oldpokedex.data.model.EncounterInGame
import br.iwan.oldpokedex.data.model.Location
import br.iwan.oldpokedex.data.model.UiResponse
import br.iwan.oldpokedex.data.model.VersionDetails
import br.iwan.oldpokedex.data.remote.PokemonService
import br.iwan.oldpokedex.data.remote.model.ApiRequestResult
import br.iwan.oldpokedex.data.remote.model.PokemonLocationResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.util.UUID
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val json: Json,
    private val service: PokemonService,
    private val dao: PokemonLocationDao
) {
    suspend fun findByPokemonId(pokemonId: Int): UiResponse<PokemonLocationEntity> {
        return withContext(ioDispatcher) {
            dao.findByPokemonId(pokemonId)?.let {
                it.locations?.let { _ ->
                    UiResponse.Success(it)
                }
            } ?: getLocationsFromRemote(pokemonId)
        }
    }

    private suspend fun getLocationsFromRemote(pokemonId: Int) =
        service.getEncountersDetails(pokemonId).let { res ->
            when (res) {
                is ApiRequestResult.Success -> {
                    val pokeLocations =
                        json.decodeFromString<List<PokemonLocationResponse.Data>>(res.data)

                    PokemonLocationEntity(UUID.randomUUID(), pokemonId).apply {
                        locations = pokeLocations.map { l ->
                            Location(
                                area = l.locationArea?.name,
                                versions = l.versions?.map { vd ->
                                    VersionDetails(
                                        version = vd.version?.name,
                                        encounters = vd.encounters?.map { e ->
                                            EncounterInGame(
                                                method = e.method?.name,
                                                condition = null,
                                                chance = e.chance,
                                                minLevel = e.minLevel,
                                                maxLevel = e.maxLevel
                                            )
                                        }
                                    )
                                }
                            )
                        }
                    }.let {
                        persist(it)
                        UiResponse.Success(it)
                    }
                }

                is ApiRequestResult.Error ->
                    UiResponse.Error(res.throwable.message.orEmpty())
            }
        }

    private suspend fun persist(location: PokemonLocationEntity) {
        withContext(ioDispatcher) {
            dao.insert(location)
        }
    }
}
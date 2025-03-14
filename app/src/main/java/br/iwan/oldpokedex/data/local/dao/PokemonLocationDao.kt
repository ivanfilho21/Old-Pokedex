package br.iwan.oldpokedex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.iwan.oldpokedex.data.local.entity.PokemonLocationEntity

@Dao
interface PokemonLocationDao {
    @Query("SELECT * FROM location WHERE pokemonId = :pokemonId")
    fun findByPokemonId(pokemonId: Int): PokemonLocationEntity?

    @Insert
    fun insert(list: PokemonLocationEntity)
}
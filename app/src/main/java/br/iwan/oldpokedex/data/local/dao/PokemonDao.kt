package br.iwan.oldpokedex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.iwan.oldpokedex.data.local.entity.PokemonEntity

@Dao
interface PokemonDao {
    @Query("SELECT * FROM pokemon")
    fun getAll(): List<PokemonEntity>

    @Query("SELECT * FROM pokemon WHERE name LIKE '%' || :name || '%'")
    fun searchByName(name: String): List<PokemonEntity>

    @Query("SELECT * FROM pokemon WHERE name LIKE :name limit 1")
    fun findByName(name: String): PokemonEntity

    @Insert
    fun insertAll(vararg pokemon: PokemonEntity)

}
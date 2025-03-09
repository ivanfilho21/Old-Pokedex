package br.iwan.oldpokedex.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String? = null,
    @ColumnInfo val description: String? = null,
    @ColumnInfo val type1: String? = null,
    @ColumnInfo val type2: String? = null,
    @ColumnInfo val height: Int? = null,
    @ColumnInfo val weight: Int? = null,
)
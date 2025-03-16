package br.iwan.oldpokedex.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey val uuid: UUID,
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo val name: String? = null,
    @ColumnInfo var description: String? = null,
    @ColumnInfo var type1: String? = null,
    @ColumnInfo var type2: String? = null,
    @ColumnInfo var height: Int? = null,
    @ColumnInfo var weight: Int? = null,
    @ColumnInfo var stats: List<Stat>? = null
)
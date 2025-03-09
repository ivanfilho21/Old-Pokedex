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
    @ColumnInfo val description: String? = null,
    @ColumnInfo val type1: String? = null,
    @ColumnInfo val type2: String? = null,
    @ColumnInfo val height: Int? = null,
    @ColumnInfo val weight: Int? = null
)
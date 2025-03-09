package br.iwan.oldpokedex.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.iwan.oldpokedex.data.model.VersionDetails
import java.util.UUID

@Entity(tableName = "location")
data class PokemonLocationEntity(
    @PrimaryKey val uuid: UUID,
    @ColumnInfo val pokemonId: Int = 0,
    @ColumnInfo val area: String? = null,
    @ColumnInfo val versions: List<VersionDetails>? = null
)
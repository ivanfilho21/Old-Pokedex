package br.iwan.oldpokedex.data.local.entity

import androidx.room.TypeConverter
import br.iwan.oldpokedex.data.model.EncounterInGame
import br.iwan.oldpokedex.data.model.VersionDetails
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromVersionDetailsList(list: List<VersionDetails>): String = Json.encodeToString(list)

    @TypeConverter
    fun toVersionDetailsList(data: String): List<VersionDetails> = Json.decodeFromString(data)

    @TypeConverter
    fun fromEncounterInGameList(list: List<EncounterInGame>): String = Json.encodeToString(list)

    @TypeConverter
    fun toEncounterInGameList(data: String): List<EncounterInGame> = Json.decodeFromString(data)
}
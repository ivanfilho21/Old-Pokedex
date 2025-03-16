package br.iwan.oldpokedex.data.local.entity

import androidx.room.TypeConverter
import br.iwan.oldpokedex.data.model.Location
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromLocationList(list: List<Location>): String = Json.encodeToString(list)

    @TypeConverter
    fun toLocationList(data: String): List<Location> = Json.decodeFromString(data)

    @TypeConverter
    fun fromStatList(list: List<Stat>): String = Json.encodeToString(list)

    @TypeConverter
    fun toStatList(data: String): List<Stat> = Json.decodeFromString(data)
}
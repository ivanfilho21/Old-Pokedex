package br.iwan.oldpokedex.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.iwan.oldpokedex.data.local.dao.PokemonDao
import br.iwan.oldpokedex.data.local.entity.PokemonEntity

@Database(entities = [PokemonEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        lateinit var instance: AppDatabase
            private set

        /**
         * Creates the database instance. This method should be invoked only once.
         *
         * Sample:
         * ```
         * class MyApplication : Application() {
         *     fun onCreate() {
         *         // Create the DB instance
         *         AppDatabase.createInstance()
         *     }
         * }
         * ```
         */
        fun createDatabaseInstance(applicationContext: Context) {
            instance = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "poke.dex.db"
            ).build()
        }
    }

    abstract fun pokemonDao(): PokemonDao

}
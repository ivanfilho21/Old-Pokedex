package br.iwan.oldpokedex.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import br.iwan.oldpokedex.data.local.dao.PokemonDao
import br.iwan.oldpokedex.data.local.dao.PokemonLocationDao
import br.iwan.oldpokedex.data.local.entity.Converters
import br.iwan.oldpokedex.data.local.entity.PokemonEntity
import br.iwan.oldpokedex.data.local.entity.PokemonLocationEntity

@Database(
    entities = [PokemonEntity::class, PokemonLocationEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE pokemon ADD COLUMN favorite INT NOT NULL DEFAULT 0")
            }
        }

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
            )
                .addMigrations(MIGRATION_1_2)
                .build()
        }
    }

    abstract fun pokemonDao(): PokemonDao

    abstract fun pokemonLocationDao(): PokemonLocationDao

}
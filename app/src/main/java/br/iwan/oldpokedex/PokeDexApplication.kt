package br.iwan.oldpokedex

import android.app.Application
import br.iwan.oldpokedex.data.local.AppDatabase

class PokeDexApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Init database
        AppDatabase.createDatabaseInstance(applicationContext)
    }
}
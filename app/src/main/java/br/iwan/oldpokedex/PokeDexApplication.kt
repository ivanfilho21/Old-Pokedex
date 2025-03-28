package br.iwan.oldpokedex

import android.app.Application
import br.iwan.oldpokedex.data.local.AppDatabase
import br.iwan.oldpokedex.ui.player.AppPlayer
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PokeDexApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Init database
        AppDatabase.createDatabaseInstance(applicationContext)

        // Init player
        AppPlayer.createPlayerInstance(applicationContext)
    }
}
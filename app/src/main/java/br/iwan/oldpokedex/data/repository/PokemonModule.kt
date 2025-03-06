package br.iwan.oldpokedex.data.repository

import br.iwan.oldpokedex.data.local.AppDatabase
import br.iwan.oldpokedex.data.local.dao.PokemonDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object PokemonModule {

    @Provides
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun providePokemonDao(): PokemonDao = AppDatabase.instance.pokemonDao()

}
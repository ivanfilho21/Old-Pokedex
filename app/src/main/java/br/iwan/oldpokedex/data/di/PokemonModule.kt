package br.iwan.oldpokedex.data.di

import br.iwan.oldpokedex.data.json.MyJsonProvider
import br.iwan.oldpokedex.data.local.AppDatabase
import br.iwan.oldpokedex.data.local.dao.PokemonDao
import br.iwan.oldpokedex.data.local.dao.PokemonLocationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json

@Module
@InstallIn(SingletonComponent::class)
object PokemonModule {

    @Provides
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun providePokemonDao(): PokemonDao = AppDatabase.instance.pokemonDao()

    @Provides
    fun providePokemonLocationDao(): PokemonLocationDao = AppDatabase.instance.pokemonLocationDao()

    @Provides
    fun provideJson(): Json = MyJsonProvider.json
}
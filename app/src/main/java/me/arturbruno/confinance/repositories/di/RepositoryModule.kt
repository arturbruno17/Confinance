package me.arturbruno.confinance.repositories.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import me.arturbruno.confinance.database.ConfinanceDatabase

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): ConfinanceDatabase =
        ConfinanceDatabase.getDatabase(context)

    @Provides
    fun ioDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
package com.stavro_xhardha.pockettreasure.ui.names

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.stavro_xhardha.pockettreasure.model.CoroutineDispatcher
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.pockettreasure.ui.FragmentScope
import dagger.Module
import dagger.Provides

@Module
class NamesFragmentModule(private val context: Context) {

    @Provides
    @FragmentScope
    fun provideNamesFragmentContext(): Context = context

    @Provides
    @FragmentScope
    fun provideViewModelFactory(
        repository: NamesRepository,
        coroutineDispatcher: CoroutineDispatcher
    ): NamesViewModelProviderFactory {
        return NamesViewModelProviderFactory(repository, coroutineDispatcher)
    }

    @Provides
    @FragmentScope
    fun provideNamesRepository(treasureApi: TreasureApi): NamesRepository = NamesRepository(treasureApi)

    @Provides
    @FragmentScope
    fun provideLayoutManager(context: Context): LinearLayoutManager = LinearLayoutManager(context)

    @Provides
    @FragmentScope
    fun provideNamesAdapter() = NamesAdapter(ArrayList())
}
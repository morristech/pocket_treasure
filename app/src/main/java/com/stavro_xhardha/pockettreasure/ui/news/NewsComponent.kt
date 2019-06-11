package com.stavro_xhardha.pockettreasure.ui.news

import com.stavro_xhardha.pockettreasure.dependency_injection.PocketTreasureComponent
import com.stavro_xhardha.pockettreasure.dependency_injection.FragmentScope
import dagger.Component

@FragmentScope
@Component(modules = [NewsFragmentModule::class], dependencies = [PocketTreasureComponent::class])
interface NewsComponent {
    fun inject(newsFragment: NewsFragment)
}
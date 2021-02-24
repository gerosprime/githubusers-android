package com.glennrosspascual.githubusers.dagger

import androidx.lifecycle.ViewModelProvider
import com.glennrosspascual.githubusers.viewmodel.DefaultViewModelFactory
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface ViewModelFactoryModule {
    @Binds
    @Singleton
    fun bindViewModelFactory(factory: DefaultViewModelFactory):
            ViewModelProvider.Factory
}
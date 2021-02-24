package com.glennrosspascual.githubusers.viewmodel

import androidx.lifecycle.ViewModel
import com.glennrosspascual.githubusers.dagger.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(GithubUsersListViewModel::class)
    fun provideSearchViewModel
                (viewModel : GithubUsersListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GithubUserDetailViewModel::class)
    fun provideDetailViewModel
                (viewModel : GithubUserDetailViewModel): ViewModel

}
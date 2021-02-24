package com.glennrosspascual.githubusers.interactors

import com.glennrosspascual.githubusers.model.repository.GithubUserRepository
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Named
import javax.inject.Singleton

@Module
class UseCaseModule {
    @Provides
    @Singleton
    fun provideUsersLoadUseCase(repository: GithubUserRepository,
                              @Named("uiScheduler") uiScheduler: Scheduler? = null,
                              @Named("networkScheduler") networkScheduler: Scheduler? = null)
        = GithubUsersLoadUseCase(repository, uiScheduler, networkScheduler)
    @Provides
    @Singleton
    fun provideSearchlUseCase(repository: GithubUserRepository,
                              @Named("uiScheduler") uiScheduler: Scheduler? = null,
                              @Named("networkScheduler") networkScheduler: Scheduler? = null)
        = GithubUsersSearchUseCase(repository, uiScheduler, networkScheduler)
}
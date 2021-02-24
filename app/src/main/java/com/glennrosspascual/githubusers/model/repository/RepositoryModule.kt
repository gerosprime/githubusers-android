package com.glennrosspascual.githubusers.model.repository

import com.glennrosspascual.githubusers.database.GithubUsersDao
import com.glennrosspascual.githubusers.http.GithubWebService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideDefaultGithubUserRepository(webService: GithubWebService,
                                         database: GithubUsersDao)
            : GithubUserRepository = DefaultGithubUserRepository(webService, database)

    companion object {
        const val PAGE_SIZE = 10
    }


}
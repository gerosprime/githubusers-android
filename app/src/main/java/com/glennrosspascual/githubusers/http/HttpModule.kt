package com.glennrosspascual.githubusers.http

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class HttpModule {

    @Provides
    @Singleton
    fun provideGithubApi() : GithubApi {
        return GithubApiRetrofit(Retrofit.Builder())
    }

    @Provides
    @Singleton
    fun provideWebservice(api : GithubApi) : GithubWebService {
        return api.githubWebService
    }

}
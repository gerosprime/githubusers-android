package com.glennrosspascual.githubusers.http

import com.glennrosspascual.githubusers.model.GithubConnectionPinger
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class HttpModule {

    companion object {
        const val PING_URL = "https://api.github.com"
    }

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

    @Provides
    @Singleton
    fun providePinger() = GithubConnectionPinger(PING_URL)

}
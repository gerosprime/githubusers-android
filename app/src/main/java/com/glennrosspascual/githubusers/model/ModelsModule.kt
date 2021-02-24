package com.glennrosspascual.githubusers.model

import com.glennrosspascual.githubusers.database.DatabaseModule
import com.glennrosspascual.githubusers.http.HttpModule
import com.glennrosspascual.githubusers.model.repository.RepositoryModule
import dagger.Module

@Module(includes = [RepositoryModule::class, DatabaseModule::class, HttpModule::class])
interface ModelsModule
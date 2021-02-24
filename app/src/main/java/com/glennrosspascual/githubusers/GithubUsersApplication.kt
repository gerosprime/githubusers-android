package com.glennrosspascual.githubusers

import android.app.Application
import com.glennrosspascual.githubusers.dagger.ApplicationComponent
import com.glennrosspascual.githubusers.dagger.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class GithubUsersApplication : Application(), HasAndroidInjector {

    @Inject
    lateinit var injector : DispatchingAndroidInjector<Any>

    lateinit var component : ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        component = DaggerApplicationComponent.builder().application(this)
            .build()
        component.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = injector

}
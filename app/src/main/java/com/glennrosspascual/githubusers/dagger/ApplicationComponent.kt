package com.glennrosspascual.githubusers.dagger

import android.app.Application
import com.glennrosspascual.githubusers.GithubUsersApplication
import com.glennrosspascual.githubusers.interactors.UseCaseModule
import com.glennrosspascual.githubusers.model.ModelsModule
import com.glennrosspascual.githubusers.rx.RxModule

import com.glennrosspascual.githubusers.viewmodel.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [ViewModelFactoryModule::class,
    ActivityBuilders::class, FragmentBuilders::class,
    ViewModelModule::class, ModelsModule::class, RxModule::class, UseCaseModule::class,
    AndroidInjectionModule::class, AndroidSupportInjectionModule::class])
interface ApplicationComponent {

    fun inject(application: GithubUsersApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application) : Builder

        fun build(): ApplicationComponent
    }

}
package com.glennrosspascual.githubusers.dagger

import com.glennrosspascual.githubusers.GithubUsersActivity

import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityBuilders {

    @ContributesAndroidInjector
    abstract fun contributeMainActivity() : GithubUsersActivity

}
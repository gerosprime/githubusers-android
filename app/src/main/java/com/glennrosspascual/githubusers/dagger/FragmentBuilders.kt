package com.glennrosspascual.githubusers.dagger

import com.glennrosspascual.githubusers.GithubUserDetailFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilders {

    @ContributesAndroidInjector
    abstract fun contributeGithubUserDetailFragment() : GithubUserDetailFragment

}
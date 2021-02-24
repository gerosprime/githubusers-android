package com.glennrosspascual.githubusers.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.glennrosspascual.githubusers.FakeGithubUserRepository
import com.glennrosspascual.githubusers.interactors.GithubUsersLoadUseCase
import com.glennrosspascual.githubusers.interactors.GithubUsersSearchUseCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GithubUsersListViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var subject : GithubUsersListViewModel
    private lateinit var useCase : GithubUsersLoadUseCase
    private lateinit var searchUseCase : GithubUsersSearchUseCase

    private lateinit var fakeGithubUserRepository: FakeGithubUserRepository
    @Before
    fun init() {
        useCase = GithubUsersLoadUseCase(fakeGithubUserRepository)
        subject = GithubUsersListViewModel(useCase, searchUseCase)
    }

    @Test
    fun loadItems_initialPage_success_itemsReturned() {
        subject.loadGithubUsers(0)
    }

}
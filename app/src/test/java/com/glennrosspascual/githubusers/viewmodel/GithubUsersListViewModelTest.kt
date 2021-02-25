package com.glennrosspascual.githubusers.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.glennrosspascual.githubusers.FakeGithubUserRepository
import com.glennrosspascual.githubusers.interactors.GithubConnectionCheckUseCase
import com.glennrosspascual.githubusers.interactors.GithubUsersLoadUseCase
import com.glennrosspascual.githubusers.interactors.GithubUsersSearchUseCase
import com.glennrosspascual.githubusers.model.GithubConnectionPinger
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GithubUsersListViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var subject : GithubUsersListViewModel
    private lateinit var useCase : GithubUsersLoadUseCase
    private lateinit var searchUseCase : GithubUsersSearchUseCase
    private lateinit var connectionUseCase : GithubConnectionCheckUseCase

    private lateinit var connectionPinger: GithubConnectionPinger
    private lateinit var fakeGithubUserRepository: FakeGithubUserRepository
    @Before
    fun init() {
        useCase = GithubUsersLoadUseCase(fakeGithubUserRepository)
        searchUseCase = GithubUsersSearchUseCase(fakeGithubUserRepository)
        connectionUseCase = GithubConnectionCheckUseCase(connectionPinger)
        subject = GithubUsersListViewModel(useCase, searchUseCase, connectionUseCase)
    }

    @Test
    fun loadItems_initialPage_success_itemsReturned() {
        subject.loadGithubUsers(0)
    }

}
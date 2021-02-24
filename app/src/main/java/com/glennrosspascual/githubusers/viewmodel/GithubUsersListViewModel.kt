package com.glennrosspascual.githubusers.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.glennrosspascual.githubusers.interactors.GithubUsersLoadUseCase
import com.glennrosspascual.githubusers.interactors.GithubUsersSearchUseCase
import com.glennrosspascual.githubusers.model.GithubUser
import com.glennrosspascual.githubusers.model.Result
import com.glennrosspascual.githubusers.model.repository.PostNoteResult
import javax.inject.Inject
import kotlin.math.ceil


class GithubUsersListViewModel @Inject constructor(
    private val loadUseCase: GithubUsersLoadUseCase,
    private val searchUseCase: GithubUsersSearchUseCase
) : ViewModel() {

    val itemsLiveData : LiveData<Result<List<GithubUser>>>
        get() = itemsMLD
    private val itemsMLD = MutableLiveData<Result<List<GithubUser>>>()

    val searchResultLiveData : LiveData<Result<List<GithubUser>>>
        get() = searchItemsMLD
    private val searchItemsMLD = MutableLiveData<Result<List<GithubUser>>>()

    val noteResultLiveData : LiveData<Result<PostNoteResult>>
        get() = favoriteResultMLD

    private val favoriteResultMLD = MutableLiveData<Result<PostNoteResult>>()

    private var _pageSize : Int = 10
    val pageSize : Int get() = _pageSize

    private var loadedUsers : List<GithubUser> = listOf()

    init {
        loadUseCase.let {
            it.complete = this::githubUsersLoadComplete
            it.result = this::githubUsersLoaded
            it.error = this::githubUsersLoadError
        }
        searchUseCase.let {
            it.result = this::searchResultsLoaded
            it.error = this::searchResultsLoadError
        }
    }

    private fun searchResultsLoadError(error : Throwable) {
        searchItemsMLD.value = Result.Error(error)
    }

    private fun searchResultsLoaded(result : List<GithubUser>) {
        searchItemsMLD.value = Result.Success(result)
    }

    fun updatePageSize(itemHeight : Int,
                       containerHeight : Int) {
        _pageSize = ceil(containerHeight.toFloat() / itemHeight.toFloat()).toInt()
    }

    fun searchOnUser(term : String) {
        searchItemsMLD.value = Result.Loading
        searchUseCase.search(term)
    }

    fun loadGithubUsers(offset : Int = 0, currentItems: List<GithubUser> = listOf()) {

        if (itemsMLD.value == Result.Loading) {
            return
        }

        itemsMLD.value = Result.Loading
        loadUseCase.loadInitial(offset, _pageSize, currentItems)

    }

    private fun githubUsersLoadComplete() {
        itemsMLD.value = Result.Success(loadedUsers)
    }

    private fun githubUsersLoaded(githubUsers : List<GithubUser>) {
        loadedUsers = githubUsers
        itemsMLD.value = Result.Loaded(githubUsers)
    }

    private fun githubUsersLoadError(error : Throwable) {
        itemsMLD.value = Result.Error(error as Exception)
    }

    override fun onCleared() {
        loadUseCase.stop()
        super.onCleared()
    }

}
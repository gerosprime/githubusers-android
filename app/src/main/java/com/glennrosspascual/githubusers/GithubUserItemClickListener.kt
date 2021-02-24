package com.glennrosspascual.githubusers

import com.glennrosspascual.githubusers.model.GithubUser


interface GithubUserItemClickListener {
    fun onUserItemClicked(githubUser: GithubUser, index : Int)
}
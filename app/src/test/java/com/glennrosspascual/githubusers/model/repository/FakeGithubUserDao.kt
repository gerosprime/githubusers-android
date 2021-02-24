package com.glennrosspascual.githubusers.model.repository

import com.glennrosspascual.githubusers.database.GithubUsersDao
import com.glennrosspascual.githubusers.model.GithubUser


class FakeGithubUserDao : GithubUsersDao {

    var getAllUsersPaginatedCalls = 0
    var getAllUsersFakeResult : List<GithubUser> = listOf()

    var insertUsersCalls = 0

    var getUsersNotesCalls = 0
    var getUsersNotesFakeResult : List<GithubUser> = listOf()

    var searchUsersByUsernameOrNotesCalls = 0
    var searchUsersFakeResult : List<GithubUser> = listOf()

    override fun getUsersRecord(): List<GithubUser> {
        return getAllUsersFakeResult
    }

    override fun getUsersRecordOffLimit(offset: Int, size: Int): List<GithubUser> {
        return getAllUsersFakeResult
    }

    override fun insertUsers(items: List<GithubUser>) {
        insertUsersCalls += 1
    }

    override fun updateGithubUser(notes: String, login: String) {

    }

    override fun updateGithubUser(notes: String, usersLogins: List<String>) {
        TODO("Not yet implemented")
    }

    override fun getGithubUserRecord(login: String): GithubUser {
        TODO("Not yet implemented")
    }

    override fun searchUsersByUsernameOrNotes(
        term: String,
        wildcardTerm: String
    ): List<GithubUser> {
        TODO("Not yet implemented")
    }
}
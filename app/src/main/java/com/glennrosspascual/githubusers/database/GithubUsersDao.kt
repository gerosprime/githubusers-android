package com.glennrosspascual.githubusers.database

import androidx.room.*
import com.glennrosspascual.githubusers.model.GithubUser

@Dao
interface GithubUsersDao {

    @Query("select * from GithubUser")
    @Transaction
    fun getUsersRecord() : List<GithubUser>

    @Query("select * from GithubUser order by id LIMIT :size OFFSET :offset ")
    @Transaction
    fun getUsersRecordOffLimit(offset : Int, size : Int) : List<GithubUser>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Transaction
    fun insertUsers(items : List<GithubUser>)

    @Query("update GithubUser set notes = :notes where login = :login")
    fun updateGithubUser(notes : String, login : String)

    @Query("update GithubUser set notes = :notes where login in(:usersLogins)")
    fun updateGithubUser(notes : String, usersLogins : List<String>)

    @Query("select * from GithubUser where login = :login")
    fun getGithubUserRecord(login : String) : GithubUser

    @Query("select * from GithubUser where (login = :term or notes = :term) or notes like :wildcardTerm" +
            " order by id")
    fun searchUsersByUsernameOrNotes(term : String, wildcardTerm : String) : List<GithubUser>

}
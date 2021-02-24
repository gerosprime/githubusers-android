package com.glennrosspascual.githubusers.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.glennrosspascual.githubusers.model.GithubUser

@Database(entities = [GithubUser::class], version = 1)
abstract class GithubUsersRoomDatabase : RoomDatabase() {
    abstract val githubUsersDao : GithubUsersDao
}
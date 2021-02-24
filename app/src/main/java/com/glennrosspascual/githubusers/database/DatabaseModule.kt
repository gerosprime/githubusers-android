package com.glennrosspascual.githubusers.database

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {

    @Provides
    fun provideDatabase(application: Application) : GithubUsersRoomDatabase {
        return Room.databaseBuilder(application,
                    GithubUsersRoomDatabase::class.java,
            "githubUsersDB.db")
            .build()
    }

    @Provides
    fun provideDao(db : GithubUsersRoomDatabase) : GithubUsersDao {
        return db.githubUsersDao
    }

}
package com.glennrosspascual.githubusers.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.glennrosspascual.githubusers.model.GithubUser
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AndroidGithubUsersDaoTest {

    private lateinit var subject : GithubUsersDao
    private lateinit var database: GithubUsersRoomDatabase

    private lateinit var originalItems: List<GithubUser>

    @Before
    fun setup() {

        val user1 = GithubUser(0 ,0, "onefine", "Ipsumo")
        val user2 = GithubUser(1 ,1, "ipsu2", "Lorem ipsum")
        val user3 = GithubUser(2 ,2, "funk3", "ipsumer")
        val user4 = GithubUser(3 ,3, "funk4", "Hello")
        val user5 = GithubUser(4 ,4, "ipsumer", "")

        originalItems = listOf(user1, user2, user3, user4, user5)

        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, GithubUsersRoomDatabase::class.java).build()
        subject = database.githubUsersDao
    }

    @Test
    fun insertOrUpdateUsers_insertionSuccess_shouldBeSameWithOriginal() {

        subject.insertUsers(originalItems)

        val result = subject.getUsersRecord()
        assertTrue(originalItems.size == result.size)
        assertTrue(result.containsAll(originalItems))

    }

    @Test
    fun searchUsersWithLoginOrNotes_success_resultSameOnTheExpected() {

        val user1 = GithubUser(0 ,0, "onefine", notes = "ipsumo")
        val user2 = GithubUser(1 ,1, "ipsu2", notes = "Lorem ipsum")
        val user3 = GithubUser(2 ,2, "funk3", notes =  "ipsumer")
        val user4 = GithubUser(3 ,3, "funk4", notes =  "Hello")
        val user5 = GithubUser(4 ,4, "ipsum", notes =  "")

        val expectedResult = listOf(user1, user2, user3, user5)
        subject.insertUsers(expectedResult)
        val searchResult = subject.searchUsersByUsernameOrNotes("ipsum", "%ipsum%")

        assertTrue(expectedResult.size == searchResult.size)
        assertTrue(searchResult.containsAll(expectedResult))

    }

    @Test
    fun getUsersPaginated_success_resultSameOnTheExpected() {

        val user1 = GithubUser(0 ,0, "onefine", "ipsumo")
        val user2 = GithubUser(1 ,1, "ipsu2", "Lorem ipsum")
        val user3 = GithubUser(2 ,2, "funk3", "ipsumer")
        val user4 = GithubUser(3 ,3, "funk4", "Hello")
        val user5 = GithubUser(4 ,4, "ipsum", "")
        val user6 = GithubUser(5,5, "loki", "ipsumo")
        val user7 = GithubUser(6 ,6, "fenris", "Lorem ipsum")
        val user8 = GithubUser(7 ,7, "odin", "ipsumer")
        val user9 = GithubUser(8 ,8, "zeus", "Hello")
        val user10 = GithubUser(9 ,9, "absalom", "")
        val user11 = GithubUser(10 ,10, "spagman", "ipsumo")
        val user12 = GithubUser(11 ,11, "pepsiman", "Lorem ipsum")
        val user13 = GithubUser(12 ,12, "cokeman", "ipsumer")
        val user14 = GithubUser(13 ,13, "andriod9", "Hello")
        val user15 = GithubUser(14 ,14, "level14", "")

        val allItems = listOf(user1, user2, user3, user4, user5, user6, user7,
                user8, user9, user10, user11, user12, user13, user14, user15)
        subject.insertUsers(allItems)

        val expectedResult = listOf(user3, user4, user5, user6, user7,
                user8, user9, user10, user11, user12)

        val getUsersResult = subject.getUsersRecordOffLimit(2, 10)
        assertTrue(getUsersResult.size == expectedResult.size)
        assertTrue(getUsersResult.containsAll(expectedResult))

    }

}
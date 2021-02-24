package com.glennrosspascual.githubusers.model.repository

import com.glennrosspascual.githubusers.model.GithubUser
import io.reactivex.rxjava3.observers.TestObserver

import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import java.lang.Exception

class DefaultGithubUserRepositoryTest {

    private lateinit var fakeWebService: FakeGithubUsersWebService
    private lateinit var fakeGithubUsersDao: FakeGithubUserDao

    private lateinit var subject : GithubUserRepository

    private lateinit var databaseItems : List<GithubUser>
    private lateinit var remoteItems : List<GithubUser>

    private val user1 = GithubUser(0 ,0, "onefine", "Ipsumo")
    private val user2 = GithubUser(1 ,1, "ipsu2", "Lorem ipsum")
    private val user3 = GithubUser(2 ,2, "funk3", "ipsumer")
    private val user4 = GithubUser(3 ,3, "funk4", "Hello")
    private val user5 = GithubUser(4 ,4, "ipsumer", "Ipsum Note")

    private val remoteUser1 = GithubUser(0 ,0, "onefine", "Ipsumo")
    private val remoteUser2 = GithubUser(1 ,1, "ipsu2", "Lorem ipsum")
    private val remoteUser3 = GithubUser(2 ,2, "funk3", "ipsumer")
    private val remoteUser4 = GithubUser(3 ,3, "funk4", "Hello")
    private val remoteUser5 = GithubUser(4 ,4, "ipsumer", "Ipsum Note")


    @Before
    fun setUp() {

        databaseItems = listOf(user1, user2, user3, user4, user5)
        remoteItems = listOf(remoteUser1, remoteUser2, remoteUser3, remoteUser4, remoteUser5)

        fakeWebService = FakeGithubUsersWebService()
        fakeGithubUsersDao = FakeGithubUserDao()
        subject = DefaultGithubUserRepository(fakeWebService, fakeGithubUsersDao)
    }


    @Test
    fun loadGithubUsers_noItemsInDbRemoteItemsExists_savedInDBRemoteItemsReturned() {

        val expectedItems = listOf(remoteUser1, remoteUser2, remoteUser3)
        fakeWebService.error = false
        fakeWebService.mockSuccessResponse = expectedItems

        val observable = subject.loadGithubUsers(0, listOf(), 3)

        val databaseItems = observable.blockingFirst()
        assertTrue(databaseItems.containsAll(expectedItems))
        assertTrue(fakeGithubUsersDao.insertUsersCalls > 0)
    }

    @Test
    fun loadGithubUsers_hasItemsInDBandRemoteHasError_savedInDBAndDBItemsReturned() {

        val expectedItems = listOf(remoteUser1, remoteUser2, remoteUser3)
        fakeWebService.error = true
        fakeGithubUsersDao.getAllUsersFakeResult = expectedItems

        val observable = subject.loadGithubUsers(0, listOf(), 3)

        val remoteItemsResult = observable.blockingFirst()
        assertTrue(remoteItemsResult.containsAll(expectedItems))
        assertTrue(fakeGithubUsersDao.insertUsersCalls == 0)
    }

    @Test
    fun loadGithubUsers_noItemsInDBandRemoteHasError_errorReturned() {

        fakeWebService.error = true

        val observer = TestObserver<List<GithubUser>>()
        subject.loadGithubUsers(0, listOf(), 3).subscribe(observer)

        observer.assertError(HttpException::class.java)

    }

}
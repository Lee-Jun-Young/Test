package com.example.test.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.test.data.network.GithubService
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class RemoteRepositoryTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var service: GithubService

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        // TODO
    }

    @Test
    fun getMyInfoData() = runTest {
        val data = service.getUserInfo()
        assertEquals(data.login, "Lee-Jun-Young")
    }

    @Test
    fun getSearchUserData() = runTest {
        val data = service.searchUser("lee-jun-young", 1)
        assertTrue(data.items.isNotEmpty())
        assertEquals(data.items[0].login, "Lee-Jun-Young")
    }

    @Test
    fun getUserByIdData() = runTest {
        val data = service.getUserById("Lee-Jun-Young")
        assertEquals(data.login, "Lee-Jun-Young")
    }
}
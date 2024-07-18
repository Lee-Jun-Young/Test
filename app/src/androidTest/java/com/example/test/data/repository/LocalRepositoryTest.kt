package com.example.test.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.test.data.dto.UserInfo
import com.example.test.data.room.BookmarkDao
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class LocalRepositoryTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var database: TestAppDatabase
    private lateinit var bookmarkDao: BookmarkDao

    @Before
    fun setup() {
        hiltRule.inject()
        bookmarkDao = database.bookmarkDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun getAllUsers() = runTest() {
        for (i in 0..2) {
            val data = UserInfo(
                login = "$i + Item",
                id = i,
                name = null,
                avatarUrl = "",
                followers = 0,
                following = 0,
                followersUrl = "",
                followingUrl = "",
                isFavorite = true
            )
            bookmarkDao.addFavorite(data)
        }

        val allData = bookmarkDao.getFavoriteAll().first()
        assertEquals(allData.size, 3)
    }

    @Test
    fun insertUser() = runTest {
        val data = UserInfo(
            login = "3 Item",
            id = 3,
            name = null,
            avatarUrl = "",
            followers = 0,
            following = 0,
            followersUrl = "",
            followingUrl = "",
            isFavorite = true
        )
        bookmarkDao.addFavorite(data)
        val allData = bookmarkDao.getFavoriteAll().first()
        assertTrue(allData.contains(data))
    }

    @Test
    fun deleteUser() = runTest {
        val data = UserInfo(
            login = "3 Item",
            id = 3,
            name = null,
            avatarUrl = "",
            followers = 0,
            following = 0,
            followersUrl = "",
            followingUrl = "",
            isFavorite = true
        )
        bookmarkDao.deleteFavorite(data)
        val allData = bookmarkDao.getFavoriteAll().first()
        assertFalse(allData.contains(data))
    }
}
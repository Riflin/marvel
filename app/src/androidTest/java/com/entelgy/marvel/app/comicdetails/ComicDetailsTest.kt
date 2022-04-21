package com.entelgy.marvel.app.comicdetails

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.entelgy.marvel.data.model.characters.ComicSummary
import com.entelgy.marvel.domain.usecases.network.comics.GetComicDetails
import com.entelgy.marvel.domain.usecases.network.comics.GetComicsFromCharacter
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ComicDetailsTest {

    @Test
    fun downloadComicData() {
        runBlocking {
            val response = GetComicDetails(1000).downloadData()

            Assert.assertTrue(response.isSuccessful)
            Assert.assertEquals(200, response.body()?.code)
            Assert.assertEquals(1, response.body()?.data?.count)
        }
    }
    @Test
    fun downloadWrongComicData() {
        runBlocking {
            val response = GetComicDetails(-1).downloadData()

            Assert.assertFalse(response.isSuccessful)
            Assert.assertEquals(404, response.code())
        }
    }

    @Test
    fun downloadComicsFromCharacter() {
        runBlocking {
            val response = GetComicsFromCharacter(1000).downloadData()

            Assert.assertTrue(response.isSuccessful)
            Assert.assertEquals(200, response.body()?.code)
            Assert.assertEquals(20, response.body()?.data?.limit)
        }
    }

    @Test
    fun getComicIdWorks() {
        val comicSummary = ComicSummary("http://gateway.marvel.com/v1/public/comics/4100", "Comic")

        Assert.assertEquals(comicSummary.getId(), 4100)
    }

    @Test
    fun getComicIdWrong() {
        val comicSummary = ComicSummary("", "Comic")

        Assert.assertNull(comicSummary.getId())
    }
}
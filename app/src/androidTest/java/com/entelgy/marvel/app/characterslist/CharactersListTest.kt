package com.entelgy.marvel.app.characterslist

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.entelgy.marvel.data.model.CharacterSummary
import com.entelgy.marvel.data.model.characters.ComicSummary
import com.entelgy.marvel.domain.usecases.network.characters.GetCharacterDetails
import com.entelgy.marvel.domain.usecases.network.characters.GetCharactersByComicFromServer
import com.entelgy.marvel.domain.usecases.network.characters.GetCharactersFromServer
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharactersListTest {

    @Test
    fun downloadCharactersData() {
        runBlocking {
            val response = GetCharactersFromServer(null, null, null).downloadData()

            Assert.assertTrue(response.isSuccessful)
            Assert.assertEquals(200, response.body()?.code)
            Assert.assertEquals(40, response.body()?.data?.count)
        }
    }
    @Test
    fun downloadCharacterDetails() {
        runBlocking {
            val response = GetCharacterDetails(1009472).downloadData()

            Assert.assertTrue(response.isSuccessful)
            Assert.assertEquals(200, response.body()?.code)
            Assert.assertEquals(1, response.body()?.data?.count)
        }
    }
    @Test
    fun downloadWrongCharacterData() {
        runBlocking {
            val response = GetCharacterDetails(-1).downloadData()

            Assert.assertFalse(response.isSuccessful)
            Assert.assertEquals(404, response.code())
        }
    }

    @Test
    fun downloadCharactersFromComic() {
        runBlocking {
            val response = GetCharactersByComicFromServer(1000, null, null, null).downloadData()

            Assert.assertTrue(response.isSuccessful)
            Assert.assertEquals(200, response.body()?.code)
            Assert.assertEquals(40, response.body()?.data?.limit)
        }
    }

    @Test
    fun getCharacterIdWorks() {
        val characterSummary = CharacterSummary("http://gateway.marvel.com/v1/public/characters/4100", "Character", "")

        Assert.assertEquals(characterSummary.getId(), 4100)
    }

    @Test
    fun getCharacterIdWrong() {
        val characterSummary = CharacterSummary("", "Lobezno", "")

        Assert.assertNull(characterSummary.getId())
    }
}
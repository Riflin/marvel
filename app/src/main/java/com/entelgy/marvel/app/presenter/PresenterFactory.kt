package com.entelgy.marvel.app.presenter

import com.entelgy.marvel.app.characterdetails.presenter.CharacterDetailsPresenter
import com.entelgy.marvel.app.characterdetails.presenter.CharacterDetailsPresenterImpl
import com.entelgy.marvel.app.characterslist.presenter.CharactersListPresenter
import com.entelgy.marvel.app.characterslist.presenter.CharactersListPresenterImpl
import com.entelgy.marvel.app.photos.presenter.PhotoPresenter
import com.entelgy.marvel.app.photos.presenter.PhotoPresenterImpl
import com.entelgy.marvel.app.webview.presenter.WebPresenter
import com.entelgy.marvel.app.webview.presenter.WebPresenterImpl

object PresenterFactory {

    private var charactersListPresenter: CharactersListPresenter? = null
    private var characterDetailsPresenter: CharacterDetailsPresenter? = null
    private var webPresenter: WebPresenter? = null
    private var photoPresenter: PhotoPresenter? = null

    fun getCharactersListPresenter(): CharactersListPresenter {
        return if (charactersListPresenter == null) {
            charactersListPresenter = CharactersListPresenterImpl()
            charactersListPresenter!!
        } else {
            charactersListPresenter!!
        }
    }

    fun getCharacterDetailsPresenter(): CharacterDetailsPresenter {
        return if (characterDetailsPresenter == null) {
            characterDetailsPresenter = CharacterDetailsPresenterImpl()
            characterDetailsPresenter!!
        } else {
            characterDetailsPresenter!!
        }
    }

    fun getWebPresenter(): WebPresenter {
        return if (webPresenter == null) {
            webPresenter = WebPresenterImpl()
            webPresenter!!
        } else {
            webPresenter!!
        }
    }

    fun getPhotoPresenter(): PhotoPresenter {
        return if (photoPresenter == null) {
            photoPresenter = PhotoPresenterImpl()
            photoPresenter!!
        } else {
            photoPresenter!!
        }
    }
}
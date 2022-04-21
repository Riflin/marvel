package com.entelgy.marvel.app.presenter

import com.entelgy.marvel.app.characterdetails.presenter.CharacterDetailsPresenter
import com.entelgy.marvel.app.characterdetails.presenter.CharacterDetailsPresenterImpl
import com.entelgy.marvel.app.charactersbycomic.presenter.CharactersByComicPresenter
import com.entelgy.marvel.app.charactersbycomic.presenter.CharactersByComicPresenterImpl
import com.entelgy.marvel.app.characterslist.CharactersListView
import com.entelgy.marvel.app.characterslist.presenter.CharactersListPresenter
import com.entelgy.marvel.app.characterslist.presenter.CharactersListPresenterImpl
import com.entelgy.marvel.app.comicdetails.dialog.presenter.ComicVariantsPresenter
import com.entelgy.marvel.app.comicdetails.dialog.presenter.ComicVariantsPresenterImpl
import com.entelgy.marvel.app.comicdetails.presenter.ComicDetailsPresenter
import com.entelgy.marvel.app.comicdetails.presenter.ComicDetailsPresenterImpl
import com.entelgy.marvel.app.photolist.presenter.PhotoListPresenter
import com.entelgy.marvel.app.photolist.presenter.PhotoListPresenterImpl
import com.entelgy.marvel.app.photos.presenter.PhotoPresenter
import com.entelgy.marvel.app.photos.presenter.PhotoPresenterImpl
import com.entelgy.marvel.app.webview.presenter.WebPresenter
import com.entelgy.marvel.app.webview.presenter.WebPresenterImpl

object PresenterFactory {

    private var charactersListPresenter: CharactersListPresenter<CharactersListView>? = null
    private var characterDetailsPresenter: CharacterDetailsPresenter? = null
    private var webPresenter: WebPresenter? = null
    private var photoPresenter: PhotoPresenter? = null
    private var comicDetailsPresenter: ComicDetailsPresenter? = null
    private var photoListPresenter: PhotoListPresenter? = null
    private var comicVariantsPresenter: ComicVariantsPresenter? = null
    private var charactersByComicPresenter: CharactersByComicPresenter? = null

    fun getCharactersListPresenter(): CharactersListPresenter<CharactersListView> {
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

    /* En este caso nos creamos una instancia del presenter por cada vista porque al poder
     * ir abriendo esta misma actividad desde la misma (pasando de un cómic a otro), el poner
     * la vista a null en el onDestroy() hacía que no funcionara bien al volver hacia atrás
     * de un cómic al que habíamos accedido desde esa misma actividad
     */
    fun getComicDetailPresenter(): ComicDetailsPresenter {
//        return if (comicDetailsPresenter == null) {
            comicDetailsPresenter = ComicDetailsPresenterImpl()
            return comicDetailsPresenter!!
//        } else {
//            comicDetailsPresenter!!
//        }
    }

    fun getPhotoListPresenter(): PhotoListPresenter {
        return if (photoListPresenter == null) {
            photoListPresenter = PhotoListPresenterImpl()
            photoListPresenter!!
        } else {
            photoListPresenter!!
        }
    }

    fun getComicVariantsPresenter(): ComicVariantsPresenter {
        return if (comicVariantsPresenter == null) {
            comicVariantsPresenter = ComicVariantsPresenterImpl()
            comicVariantsPresenter!!
        } else {
            comicVariantsPresenter!!
        }
    }

    fun getCharactersByComicPresenter(): CharactersByComicPresenter {
        return if (charactersByComicPresenter == null) {
            charactersByComicPresenter = CharactersByComicPresenterImpl()
            charactersByComicPresenter!!
        } else {
            charactersByComicPresenter!!
        }
    }
}
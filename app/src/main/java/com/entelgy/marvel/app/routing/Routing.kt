package com.entelgy.marvel.app.routing

import android.content.Context
import com.entelgy.marvel.app.characterdetails.CharacterDetailsActivity
import com.entelgy.marvel.app.comicdetails.ComicDetailsActivity
import com.entelgy.marvel.app.photolist.PhotoListActivity
import com.entelgy.marvel.app.photos.PhotoActivity
import com.entelgy.marvel.app.webview.WebActivity
import com.entelgy.marvel.data.model.Image
import com.entelgy.marvel.data.model.Url
import com.entelgy.marvel.data.model.characters.Character
import com.entelgy.marvel.data.model.comics.Comic

object Routing {

    fun goToCharacterDetailsActivity(context: Context, characterId: Int) {
        context.startActivity(CharacterDetailsActivity.createNewIntent(context, characterId))
    }

    fun goToCharacterDetailsActivity(context: Context, character: Character) {
        context.startActivity(CharacterDetailsActivity.createNewIntent(context, character))
    }

    fun goToComicDetailsActivity(context: Context, comicId: Int) {
        context.startActivity(ComicDetailsActivity.createNewIntent(context, comicId))
    }

    fun goToComicDetailsActivity(context: Context, comic: Comic) {
        context.startActivity(ComicDetailsActivity.createNewIntent(context, comic))
    }

    fun goToPhotoActivity(context: Context, url: String) {
        context.startActivity(PhotoActivity.createNewIntent(context, url))
    }

    fun goToWebActivity(context: Context, url: Url) {
        context.startActivity(WebActivity.createNewIntent(context, url))
    }

    fun goToPhotoListActivity(context: Context, images: List<Image>) {
        context.startActivity(PhotoListActivity.createNewIntent(context, images))
    }
}
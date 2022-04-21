package com.entelgy.marvel.app.routing

import android.content.Context
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.entelgy.marvel.app.characterdetails.CharacterDetailsActivity
import com.entelgy.marvel.app.charactersbycomic.CharactersByComicActivity
import com.entelgy.marvel.app.comicdetails.ComicDetailsActivity
import com.entelgy.marvel.app.comicdetails.dialog.ComicVariantsDialogFragment
import com.entelgy.marvel.app.photolist.PhotoListActivity
import com.entelgy.marvel.app.photos.PhotoActivity
import com.entelgy.marvel.app.webview.WebActivity
import com.entelgy.marvel.data.model.Image
import com.entelgy.marvel.data.model.Url
import com.entelgy.marvel.data.model.characters.Character
import com.entelgy.marvel.data.model.characters.ComicSummary
import com.entelgy.marvel.data.model.comics.Comic
import com.entelgy.marvel.data.utils.Constants

/**
 * Desde aquí controlamos los métodos con los que acceder a cada una de las pantallas de la aplicación.
 * Cuando queramos acceder a una de ellas, deberíamos llamar aquí directamente
 */
object Routing {

    fun goToCharacterDetailsActivity(context: Context, characterId: Int, characterName: String) {
        context.startActivity(CharacterDetailsActivity.createNewIntent(context, characterId, characterName))
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

    fun openComicVariantsDialog(fragmentManager: FragmentManager, variants: List<ComicSummary>) {
        val dialog = ComicVariantsDialogFragment.creanteNewInstance(variants)
        dialog.show(fragmentManager, Constants.COMIC_VARIANTS)
    }

    fun closeComicVariantsDialog(fragmentManager: FragmentManager) {
        val dialog = fragmentManager.findFragmentByTag(Constants.COMIC_VARIANTS) as? DialogFragment
        dialog?.dismiss()
    }

    fun goToCharactersByComicActivity(context: Context, comicID: Int, comicName: String) {
        context.startActivity(CharactersByComicActivity.createNewIntent(context, comicID, comicName))
    }
}
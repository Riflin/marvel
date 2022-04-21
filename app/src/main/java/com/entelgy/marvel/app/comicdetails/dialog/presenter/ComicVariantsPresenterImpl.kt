package com.entelgy.marvel.app.comicdetails.dialog.presenter

import android.os.Bundle
import com.entelgy.marvel.app.comicdetails.dialog.ComicVariantsView
import com.entelgy.marvel.app.routing.Routing
import com.entelgy.marvel.data.model.characters.ComicSummary
import com.entelgy.marvel.data.utils.Constants

class ComicVariantsPresenterImpl : ComicVariantsPresenter {
    override var view: ComicVariantsView? = null

    override fun create() {

    }

    override fun resume() {

    }

    override fun pause() {

    }

    override fun stop() {

    }

    override fun destroy() {
        view = null
    }

    override fun onComicSelected(comic: ComicSummary) {
        view?.viewContext?.let { context ->
            val id = comic.getId()
            if (id != null) {
                //Cerramos el diálogo
                view?.getSupportFragmentManager()?.let { fragmentManager ->
                    Routing.closeComicVariantsDialog(fragmentManager)
                }
                //Y abrimos una nueva activity con los detalles del comic
                Routing.goToComicDetailsActivity(context, id)
            } else {
                view?.onComicNotSelectable()
            }
        }
    }

    override fun onMoreComicsSelected() {
        //NOTHING HERE
    }

    override fun getData(arguments: Bundle?) {
        if (arguments != null) {
            val variants: ArrayList<ComicSummary>? = arguments.getParcelableArrayList(Constants.COMIC_VARIANTS)
            if (variants.isNullOrEmpty()) {
                view?.onDataError()
            } else {
                view?.showVariants(variants)
            }
        } else {
            view?.onDataError()
        }
    }
}
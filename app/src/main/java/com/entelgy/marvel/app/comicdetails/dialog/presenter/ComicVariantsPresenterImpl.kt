package com.entelgy.marvel.app.comicdetails.dialog.presenter

import android.os.Bundle
import com.entelgy.marvel.app.comicdetails.dialog.ComicVariantsView
import com.entelgy.marvel.app.routing.Routing
import com.entelgy.marvel.data.model.characters.ComicSummary
import com.entelgy.marvel.data.utils.Constants

/**
 * Presenter para el diálogo donde mosramos las variantes de un cómic
 */
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

    /**
     * Mostramos los detalles del cómic seleccionado
     */
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

    /**
     * Comprobamos que hemos recibido bien los datos para abrir el diálogo
     */
    override fun getData(arguments: Bundle?) {
        //Tenemos que tener un Bundle
        if (arguments != null) {
            //Y una lista de variantes (comicSummary)
            val variants: ArrayList<ComicSummary>? = arguments.getParcelableArrayList(Constants.COMIC_VARIANTS)
            //Si está vacía o es nula, error
            if (variants.isNullOrEmpty()) {
                view?.onDataError()
            } else {
                //Si no, pues la mostramos
                view?.showVariants(variants)
            }
        } else {
            view?.onDataError()
        }
    }
}
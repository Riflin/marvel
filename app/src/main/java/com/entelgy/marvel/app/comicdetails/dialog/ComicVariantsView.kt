package com.entelgy.marvel.app.comicdetails.dialog

import com.entelgy.marvel.app.utils.base.BaseView
import com.entelgy.marvel.data.model.characters.ComicSummary

interface ComicVariantsView: BaseView {

    fun showVariants(variants: List<ComicSummary>)
    fun onComicNotSelectable()
}
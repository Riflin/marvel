package com.entelgy.marvel.app.photolist

import com.entelgy.marvel.app.utils.base.BaseView
import com.entelgy.marvel.data.model.Image

interface PhotoListView: BaseView {

    fun showImages(images: List<Image>)
}
package com.entelgy.marvel.data.model.imageformats

/**
 * Esta clase no tiene un "formato" definido, sino que es simplemente la url + la extensión la forma
 * en la que veremos la imagen a fullSize
 */
class FullSizeImage: ImageFormat {
     override fun format(): String {
          return ""
     }
}
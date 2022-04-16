package com.entelgy.marvel.data.model.imageformats

enum class DetailImage(private val format: String): ImageFormat {
     Detail("detail");

     override fun format(): String {
          return format
     }
}
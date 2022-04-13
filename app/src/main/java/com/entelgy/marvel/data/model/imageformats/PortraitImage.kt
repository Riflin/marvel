package com.entelgy.marvel.data.model.imageformats

enum class PortraitImage(private val format: String): ImageFormat {
     Small("portrait_small"),
     Medium("portrait_medium"),
     Xlarge("portrait_xlarge"),
     Fantastic("portrait_fantastic"),
     Uncanny("portrait_uncanny"),
     Incredible("portrait_incredible");

     override fun format(): String {
          return format
     }
}
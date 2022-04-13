package com.entelgy.marvel.data.model.imageformats

enum class LandscapeImage(private val format: String): ImageFormat {
     Small("landscape_small"),
     Medium("landscape_medium"),
     Xlarge("landscape_xlarge"),
     Fantastic("landscape_fantastic"),
     Uncanny("landscape_uncanny"),
     Incredible("landscape_incredible");

     override fun format(): String {
          return format
     }
}
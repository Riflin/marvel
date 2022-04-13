package com.entelgy.marvel.data.model.imageformats

enum class StandardImage(private val format: String): ImageFormat {
     Small("standard_small"),
     Medium("standard_medium"),
     Xlarge("standard_xlarge"),
     Fantastic("standard_fantastic"),
     Uncanny("standard_uncanny"),
     Incredible("standard_incredible");

     override fun format(): String {
          return format
     }
}
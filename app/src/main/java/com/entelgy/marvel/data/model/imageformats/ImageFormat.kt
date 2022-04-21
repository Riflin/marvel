package com.entelgy.marvel.data.model.imageformats

/**
 * Interfaz que deben implementar todos los formatos de imagen disponibles
 */
interface ImageFormat {
     //Cada formato de imagen (normalmente un enum) tendrá diferentes tipos de formatos disponibles
     fun format(): String
}
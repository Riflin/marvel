package com.entelgy.marvel.data.model.utils

/**
 * Clase para enumerar los distintos roles que tienen los creadores
 */
enum class Role(val rol: String) {
    WRITER("writer"),
    PENCILLER("penciller"),
    PENCILER("penciler"),
    PENCILLER_COVER("penciller (cover)"),
    PENCILER_COVER("penciler (cover)"),
    LETTERER("letterer"),
    INKER("inker"),
    INKER_COVER("inker (cover)"),
    COLORIST("colorist"),
    COLORIST_COVER("colorist (cover)"),
    EDITOR("editor"),
    EDITOR_COVER("editor (cover)"),
    PAINTER("painter"),
    PAINTER_COVER("painter (cover)");
}
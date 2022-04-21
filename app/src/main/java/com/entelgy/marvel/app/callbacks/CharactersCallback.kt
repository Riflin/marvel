package com.entelgy.marvel.app.callbacks

import com.entelgy.marvel.data.model.CharacterSummary
import com.entelgy.marvel.data.model.characters.Character

/**
 * Callback para los adapters donde se muestran los personajes. Deben implementarlo los presenters
 * que lo necesiten
 */
interface CharactersCallback {

    fun onCharacterSelected(character: Character)

    fun onCharacterSelected(character: CharacterSummary)

    fun onMoreCharactersSelected()
}
package com.entelgy.marvel.app.callbacks

import com.entelgy.marvel.data.model.CharacterSummary
import com.entelgy.marvel.data.model.characters.Character

interface CharactersCallback {

    fun onCharacterSelected(character: Character)

    fun onCharacterSelected(character: CharacterSummary)

    fun onMoreCharactersSelected()
}
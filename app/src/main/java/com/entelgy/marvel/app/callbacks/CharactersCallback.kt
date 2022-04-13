package com.entelgy.marvel.app.callbacks

import com.entelgy.marvel.data.model.characters.Character

interface CharactersCallback {

    fun onCharacterSelected(character: Character)
}
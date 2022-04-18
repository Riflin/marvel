package com.entelgy.marvel.app.characterslist

import com.entelgy.marvel.app.utils.base.BaseView
import com.entelgy.marvel.data.model.utils.Sort
import com.entelgy.marvel.data.model.characters.Character
import java.util.*

interface CharactersListView : BaseView {

    fun onFilterNameSelected(filter: String)

    fun resetFilterName()

    fun showDeleteFilterName(show: Boolean = true)

    fun onFilterDateSelected(date: Date)

    fun resetFilterDate()

    fun showDeleteFilterDate(show: Boolean = true)

    fun onSortNameSelected(sort: Sort)

    fun showDeleteSortName(show: Boolean = true)

    fun onSortDateSelected(sort: Sort)

    fun showDeleteSortDate(show: Boolean = true)

    fun resetSortName()

    fun resetSortDate()

    fun showCharacters(characters: List<Character>)

    fun addCharacters(characters: List<Character>)

    fun showCopyright(copyright: String?)
}
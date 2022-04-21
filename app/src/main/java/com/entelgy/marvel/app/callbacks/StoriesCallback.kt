package com.entelgy.marvel.app.callbacks

import com.entelgy.marvel.data.model.StorySummary

/**
 * Callback para los adapters donde se muestran las historias. Deben implementarlo los presenters
 * que lo necesiten
 */
interface StoriesCallback {

    fun onStorySelected(story: StorySummary)

    fun onMoreStoriesSelected()
}
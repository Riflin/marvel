package com.entelgy.marvel.app.callbacks

import com.entelgy.marvel.data.model.StorySummary

interface StoriesCallback {

    fun onStorySelected(story: StorySummary)

    fun onMoreStoriesSelected()
}
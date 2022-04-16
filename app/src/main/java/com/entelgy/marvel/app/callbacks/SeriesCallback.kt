package com.entelgy.marvel.app.callbacks

import com.entelgy.marvel.data.model.SeriesSummary

interface SeriesCallback {

    fun onSeriesSelected(series: SeriesSummary)

    fun onMoreSeriesSelected()
}
package com.entelgy.marvel.app.callbacks

import com.entelgy.marvel.data.model.SeriesSummary

/**
 * Callback para los adapters donde se muestran las series. Deben implementarlo los presenters
 * que lo necesiten
 */
interface SeriesCallback {

    fun onSeriesSelected(series: SeriesSummary)

    fun onMoreSeriesSelected()
}
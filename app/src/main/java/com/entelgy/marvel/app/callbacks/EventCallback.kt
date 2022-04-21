package com.entelgy.marvel.app.callbacks

import com.entelgy.marvel.data.model.EventSummary

/**
 * Callback para los adapters donde se muestran los eventos. Deben implementarlo los presenters
 * que lo necesiten
 */
interface EventCallback {

    fun onEventSelected(event: EventSummary)

    fun onMoreEventSelected()
}
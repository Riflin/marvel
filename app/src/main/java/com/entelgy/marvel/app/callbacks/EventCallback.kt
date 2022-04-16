package com.entelgy.marvel.app.callbacks

import com.entelgy.marvel.data.model.EventSummary

interface EventCallback {

    fun onEventSelected(event: EventSummary)

    fun onMoreEventSelected()
}
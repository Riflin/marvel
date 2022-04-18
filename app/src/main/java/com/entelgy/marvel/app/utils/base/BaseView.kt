package com.entelgy.marvel.app.utils.base

import android.app.Activity
import android.content.Context
import androidx.fragment.app.FragmentManager

interface BaseView {
    fun showLoading(show: Boolean = true)
    val context: Context

    fun showError(message: String)
    fun getSupportFragmentManager(): FragmentManager

    fun onDataError()
}
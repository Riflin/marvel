package com.entelgy.marvel.app.utils.base

import android.app.Activity
import android.content.Context
import androidx.fragment.app.FragmentManager

interface BaseView {
    fun showLoading()
    fun hideLoading()
    val context: Context
    val activity: Activity

    fun showError(message: String)
    fun getSupportFragmentManager(): FragmentManager
}
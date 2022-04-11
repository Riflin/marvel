package com.entelgy.marvel.app.utils.base

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.entelgy.marvel.R
import com.entelgy.marvel.app.utils.Utils

abstract class BaseActivity: AppCompatActivity(), BaseView {

    override val context: Context?
        get() = this
    override val activity: Activity?
        get() = this

    override fun showError(message: String) {
        Utils.showDialogInformacion(supportFragmentManager, getString(R.string.error), message)
    }
}
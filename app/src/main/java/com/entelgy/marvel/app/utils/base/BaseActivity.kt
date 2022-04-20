package com.entelgy.marvel.app.utils.base

import android.content.Context
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.entelgy.marvel.R
import com.entelgy.marvel.app.utils.AppUtils

abstract class BaseActivity: AppCompatActivity(), BaseView {

    override val context: Context
        get() = this

    override fun setContentView(view: View?) {
        super.setContentView(view)

        init()

        initViews()

        attachListenersToTheViews()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }

    abstract fun init()

    abstract fun initViews()

    abstract fun attachListenersToTheViews()

    override fun showError(message: String) {
        AppUtils.showDialogInformacion(supportFragmentManager, getString(R.string.error), message)
    }

    override fun onDataError() {
        AppUtils.showDialogInformacion(supportFragmentManager, getString(R.string.error),
            getString(R.string.error_obteniendo_datos)) { finish() }
    }
}
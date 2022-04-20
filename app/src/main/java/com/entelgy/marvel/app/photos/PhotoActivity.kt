package com.entelgy.marvel.app.photos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.entelgy.marvel.R
import com.entelgy.marvel.app.photos.presenter.PhotoPresenter
import com.entelgy.marvel.app.presenter.PresenterFactory
import com.entelgy.marvel.app.utils.AppUtils
import com.entelgy.marvel.app.utils.base.BaseActivity
import com.entelgy.marvel.data.utils.Constants
import com.entelgy.marvel.databinding.ActivityPhotoBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class PhotoActivity: BaseActivity(), PhotoView {

    private lateinit var binding: ActivityPhotoBinding

    private lateinit var presenter: PhotoPresenter

    companion object {
        fun createNewIntent(context: Context, url: String): Intent {
            return Intent(context, PhotoActivity::class.java).apply {
                putExtra(Constants.URL, url)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPhotoBinding.inflate(layoutInflater)

        setContentView(binding.root)

        presenter.getData(intent)
    }

    override fun showPhoto(url: String) {
        binding.progressBar.visibility = View.VISIBLE
        Picasso.get().load(url).into(binding.ivPhoto, object: Callback {
            override fun onSuccess() {
                binding.progressBar.visibility = View.GONE
            }

            override fun onError(e: Exception?) {
                binding.progressBar.visibility = View.GONE
                binding.ivPhoto.setImageResource(R.drawable.ic_broken_image)
            }
        })
    }

    override fun onDataError() {
        AppUtils.showDialogInformacion(supportFragmentManager, getString(R.string.error),
            getString(R.string.error_obteniendo_datos)) {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun init() {
        presenter = PresenterFactory.getPhotoPresenter()
        presenter.view = this
        presenter.create()
    }

    override fun initViews() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun attachListenersToTheViews() {
        //NOTHING
    }

    override fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }
}
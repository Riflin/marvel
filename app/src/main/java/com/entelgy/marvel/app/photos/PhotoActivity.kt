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

/**
 * Pantalla que muestra una imagen en alta resolución.
 *
 * Esta pantalla podría estar fusionada con la de PhotoListActivity, pero se hizo antes que la
 * otra y ya preferí dejarla así
 */
class PhotoActivity: BaseActivity(), PhotoView {

    private lateinit var binding: ActivityPhotoBinding

    private lateinit var presenter: PhotoPresenter

    companion object {
        /**
         * Debemos pasar la url de la foto a mostrar
         */
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

        //Como siempre, el presenter se encarga de ver si hay fallos o no
        presenter.getData(intent)
    }

    /**
     * Muestra la iamgen
     */
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
        //Flechita hacia atrás habilitada (este layout no tiene toolbar propio, sino el de android)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun attachListenersToTheViews() {
        //NOTHING
    }

    override fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }
}
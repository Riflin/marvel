package com.entelgy.marvel.app.photolist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.entelgy.marvel.R
import com.entelgy.marvel.app.photolist.adapter.PhotosPagerAdapter
import com.entelgy.marvel.app.photolist.presenter.PhotoListPresenter
import com.entelgy.marvel.app.presenter.PresenterFactory
import com.entelgy.marvel.app.utils.base.BaseActivity
import com.entelgy.marvel.data.model.Image
import com.entelgy.marvel.data.utils.Constants
import com.entelgy.marvel.databinding.ActivityPhotoListBinding

/**
 * Pantalla para mostrar la lista de imágenes promocionales de un cómic
 */
class PhotoListActivity: BaseActivity(), PhotoListView {

    companion object {
        /**
         * Debemos pasarle el listado de imágenes a mostrar
         */
        fun createNewIntent(context: Context, images: List<Image>): Intent {
            return Intent(context, PhotoListActivity::class.java).apply {
                putExtra(Constants.IMAGES, ArrayList(images))
            }
        }
    }

    private lateinit var binding: ActivityPhotoListBinding

    private lateinit var presenter: PhotoListPresenter

    private var totalFotos: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Inflamos el layout
        binding = ActivityPhotoListBinding.inflate(layoutInflater)

        setContentView(binding.root)

        //Delegamos al presenter la obtención de los datos
        presenter.getData(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun init() {
        presenter = PresenterFactory.getPhotoListPresenter()
        presenter.view = this
        presenter.create()
    }

    override fun initViews() {
        //Flechita hacia atrás habilitada
        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = getString(R.string.image_list)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun attachListenersToTheViews() {
        //Al cambiar de imagen iremos actualizando el número de la parte inferior
        binding.viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                setFotoIndex(position+1)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    /**
     * Muestra el número de imagen que estamos viendo y cuántas tenemos en total
     */
    private fun setFotoIndex(position: Int) {
        binding.tvNumber.text = getString(R.string.photo_index, position, totalFotos)
    }

    override fun showImages(images: List<Image>) {
        //Nos guardamos el total de fotos en esta variable para ir cambiándolo al pasar de página en el viewPager
        totalFotos = images.size
        //Creamos el adapter
        binding.viewPager.adapter = PhotosPagerAdapter(this, images)
        //Lo inicializamos en la primera imagen
        binding.viewPager.currentItem = 0

        //Y mostramos el texto con el número de imagen en el que estamos
        setFotoIndex(1)
    }

    override fun showLoading(show: Boolean) {
        //NOTHING HERE
    }
}
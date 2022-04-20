package com.entelgy.marvel.app.photolist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.entelgy.marvel.R
import com.entelgy.marvel.data.model.Image
import com.entelgy.marvel.databinding.ItemPhotoDetailBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

/**
 * Pager adapter para mostrar las imágenes promocionales de los comics
 */
class PhotosPagerAdapter(context: Context, private val images: List<Image>): PagerAdapter() {

    private val inflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        //Inflamos la vista donde mostramos la imagen sin más
        val binding = ItemPhotoDetailBinding.inflate(inflater, container, false)

        val image = images[position]

        binding.progressBar.visibility = View.VISIBLE
        Picasso.get().load(image.getRealPath()).into(binding.ivPhoto, object: Callback {
            override fun onSuccess() {
                binding.progressBar.visibility = View.GONE
            }

            override fun onError(e: Exception?) {
                binding.progressBar.visibility = View.GONE
                binding.ivPhoto.setImageResource(R.drawable.ic_broken_image)
            }
        })

        container.addView(binding.root)

        return binding.root
    }
}
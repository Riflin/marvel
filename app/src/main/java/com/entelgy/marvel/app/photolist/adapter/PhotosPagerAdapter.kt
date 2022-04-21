package com.entelgy.marvel.app.photolist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
class PhotosPagerAdapter(context: Context, private val images: List<Image>):
    RecyclerView.Adapter<PhotosPagerAdapter.PhotoHolder>() {

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        val view = inflater.inflate(R.layout.item_photo_detail, parent, false)
        return PhotoHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        val image = images[position]

        holder.bind(image)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    inner class PhotoHolder(view: View): RecyclerView.ViewHolder(view) {

        private val binding = ItemPhotoDetailBinding.bind(view)

        fun bind(image: Image) {
            //Mostramos la imagen
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

        }

    }
}
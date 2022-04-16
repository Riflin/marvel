package com.entelgy.marvel.app.characterdetails.adapter

import android.content.Context
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.entelgy.marvel.R
import com.entelgy.marvel.app.callbacks.UrlCallback
import com.entelgy.marvel.data.model.Url
import com.entelgy.marvel.databinding.ItemUrlBinding


class UrlAdapter(context: Context, private val urls: List<Url>,
                 private val callback: UrlCallback): RecyclerView.Adapter<UrlAdapter.UrlHolder>() {

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UrlHolder {
        val view = inflater.inflate(R.layout.item_url, parent, false)
        return UrlHolder(view)
    }

    override fun onBindViewHolder(holder: UrlHolder, position: Int) {
        val url = urls[position]
        holder.bind(url)
    }

    override fun getItemCount(): Int {
        return urls.size
    }

    inner class UrlHolder(view: View): RecyclerView.ViewHolder(view) {
        private val binding = ItemUrlBinding.bind(view)

        fun bind(url: Url) {
            //Mostramos el enlace subrayado
            val content = SpannableString(url.type)
            content.setSpan(UnderlineSpan(), 0, content.length, 0)
            binding.tvName.text = content

            binding.tvName.setOnClickListener { callback.onUrlSelected(url) }
        }
    }
}
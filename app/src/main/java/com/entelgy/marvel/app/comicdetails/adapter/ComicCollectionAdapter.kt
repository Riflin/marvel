package com.entelgy.marvel.app.comicdetails.adapter

import android.content.Context
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.entelgy.marvel.R
import com.entelgy.marvel.app.callbacks.ComicsCallback
import com.entelgy.marvel.data.model.characters.ComicSummary
import com.entelgy.marvel.databinding.ItemSummaryBinding

/**
 * Adapter para mostrar tanto las colecciones en las que aparece este cómic como los cómics contenidos
 * en la colección seleccionada. Tanto uno como otro son objetos ComicSummary que mostramos aquí. En este
 * caso, no se muestra al final un campo indicando que hay X cómics más
 */
class ComicCollectionAdapter(private val context: Context, private val comics: List<ComicSummary>,
                             private val callback: ComicsCallback): RecyclerView.Adapter<ComicCollectionAdapter.ComicHolder>() {

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicHolder {
        val view = inflater.inflate(R.layout.item_summary, parent, false)
        return ComicHolder(view)
    }

    override fun onBindViewHolder(holder: ComicHolder, position: Int) {
        val comic = comics[position]
        holder.bind(comic)
    }

    override fun getItemCount(): Int {
        return comics.size
    }

    /**
     * Aunque el nombre es igual que el viewHolder del ComicSummaryAdapter, y se llama al mismo
     * callback al seleccionarlo, lo mostramos ligeramente diferente. Se podría generalizar en un
     * único viewHolder
     */
    inner class ComicHolder(view: View): RecyclerView.ViewHolder(view) {
        private val binding = ItemSummaryBinding.bind(view)

        fun bind(comic: ComicSummary) {
            //Mostramos el texto en rojo y subrayado, para indicar que podemos pulsar sobre él
            binding.tvName.setTextColor(ContextCompat.getColor(context, R.color.red_dark_selector))
            val content = SpannableString(comic.name)
            content.setSpan(UnderlineSpan(), 0, content.length, 0)

            binding.tvName.text = content

            binding.root.setOnClickListener { callback.onComicSelected(comic) }
        }
    }
}
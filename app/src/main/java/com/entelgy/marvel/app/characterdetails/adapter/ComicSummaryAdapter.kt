package com.entelgy.marvel.app.characterdetails.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.entelgy.marvel.R
import com.entelgy.marvel.app.callbacks.ComicsCallback
import com.entelgy.marvel.app.characterdetails.adapter.viewholder.MoreItemsHolder
import com.entelgy.marvel.app.utils.AppUtils
import com.entelgy.marvel.data.model.characters.ComicSummary
import com.entelgy.marvel.data.utils.Constants
import com.entelgy.marvel.databinding.ItemSummaryBinding

/**
 * Adapter para mostrar los comics en los que aparece un personaje
 *
 * El parámetro "totalComics" indica cuántos comics más hay aparte de los que mostramos
 */
class ComicSummaryAdapter(context: Context, private val comics: List<ComicSummary>, private val totalComics: Int,
                          private val callback: ComicsCallback): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //El inflater, para no estar obteniéndolo cada vez que creamos un viewHolder
    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //Tenemos dos tipos de layouts que mostrar: el de los comics y el de "más cómics"
        return when (viewType) {
            //Detalles del cómic
            Constants.TYPE_ITEM -> {
                val view = inflater.inflate(R.layout.item_summary, parent, false)
                ComicHolder(view)
            }
            //...y X cómics más
            Constants.TYPE_MORE_ITEMS -> {
                val view = inflater.inflate(R.layout.layout_more_items_available, parent, false)
                //Llamaremos al callback para que haga lo que quiera ahí al pulsar sobre el texto
                 MoreItemsHolder(view) { callback.onMoreComicsSelected() }
            }
            //NUNCA llegaremos
            else -> {
                val view = inflater.inflate(R.layout.item_summary, parent, false)
                ComicHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //Comprobamos qué viewHolder tenemos para mostrar el comic o cuántos hay más
        when (holder) {
            is ComicHolder -> {
                //Los comics los mostramos en una lista bicolor para una mejor visibilidad
                if (position % 2 == 0) {
                    holder.itemView.setBackgroundResource(R.drawable.item_character_background)
                } else {
                    holder.itemView.setBackgroundResource(R.drawable.item_character_blue_background)
                }
                val comic = comics[position]
                holder.bind(comic)
            }
            is MoreItemsHolder -> {
                holder.bind(totalComics)
            }
        }
    }

    override fun getItemCount(): Int {
        //En esta función indica si tendremos que mostrar sólo los comics o también el último campo de "...y X más"
        return AppUtils.getItemCount(comics, totalComics)
    }

    /**
     * Controlamos qué tipo de fila debemos mostrar
     */
    override fun getItemViewType(position: Int): Int {
        return when {
            //Cuando estamos mostrando la lista, pues de tipo comic
            position < comics.size -> Constants.TYPE_ITEM
            //Si es la última posición, de "...y X más"
            else -> Constants.TYPE_MORE_ITEMS
        }
    }

    inner class ComicHolder(view: View): RecyclerView.ViewHolder(view) {
        private val binding = ItemSummaryBinding.bind(view)

        /**
         * Mostramos el nombre del comic y llamamos al callback indicanado que lo hemos seleccionado
         */
        fun bind(comic: ComicSummary) {
            binding.tvName.text = comic.name

            binding.root.setOnClickListener { callback.onComicSelected(comic) }
        }
    }
}
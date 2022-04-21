package com.entelgy.marvel.app.comicdetails.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.entelgy.marvel.R
import com.entelgy.marvel.app.callbacks.CharactersCallback
import com.entelgy.marvel.app.characterdetails.adapter.viewholder.MoreItemsHolder
import com.entelgy.marvel.app.utils.AppUtils
import com.entelgy.marvel.data.model.CharacterSummary
import com.entelgy.marvel.data.utils.Constants
import com.entelgy.marvel.databinding.ItemSummaryBinding

/**
 * Adapter para mostrar los personajes que aparecen en un cómic. Tiene el mismo funcionamiento que los
 * adapters del paquete characterdetails (es decir, ComicSummaryAdapter, EventsSummaryAdapter, etc)
 */
class CharacterSummaryAdapter(context: Context, private val characters: List<CharacterSummary>, private val totalCharacters: Int,
                              private val callback: CharactersCallback): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    /**
     * Dos tipos de viewHolders: info del personaje y el de "...y X más"
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Constants.TYPE_ITEM -> {
                val view = inflater.inflate(R.layout.item_summary, parent, false)
                CharacterHolder(view)
            }
            Constants.TYPE_MORE_ITEMS -> {
                val view = inflater.inflate(R.layout.layout_more_items_available, parent,  false)
                MoreItemsHolder(view) { callback.onMoreCharactersSelected() }
            }
            else -> {
                val view = inflater.inflate(R.layout.item_summary, parent, false)
                CharacterHolder(view)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CharacterHolder -> {
                if (position % 2 == 0) {
                    holder.itemView.setBackgroundResource(R.drawable.item_character_background)
                } else {
                    holder.itemView.setBackgroundResource(R.drawable.item_character_blue_background)
                }
                val comic = characters[position]

                holder.bind(comic)
            }
            is MoreItemsHolder -> {
                holder.bind(totalCharacters)
            }
        }
    }

    override fun getItemCount(): Int {
        return AppUtils.getItemCount(characters, totalCharacters)
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position < characters.size -> Constants.TYPE_ITEM
            else -> Constants.TYPE_MORE_ITEMS
        }
    }

    inner class CharacterHolder(view: View): RecyclerView.ViewHolder(view) {
        private val binding = ItemSummaryBinding.bind(view)

        fun bind(character: CharacterSummary) {
            binding.tvName.text = character.name

            binding.root.setOnClickListener { callback.onCharacterSelected(character) }
        }
    }
}
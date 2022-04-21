package com.entelgy.marvel.app.characterdetails.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.entelgy.marvel.R
import com.entelgy.marvel.databinding.LayoutMoreItemsAvailableBinding

/**
 * ViewHolder común a los adapter de comics/series/eventos/historias del personaje, para indicar que hay
 * más objetos disponibles para ver
 */
class MoreItemsHolder(view: View, private val action : () -> Unit) : RecyclerView.ViewHolder(view) {
    //El layout es simplemente un textView indicando cuántos objetos más hay
    private val binding = LayoutMoreItemsAvailableBinding.bind(view)

    fun bind(numberOfItems: Int) {
        binding.tvMoreItems.text = itemView.context.getString(R.string.more_items_available, numberOfItems)

        //Al pulsarlo llamaremos al método del callback correspondiente para hacer lo que queramos
        binding.root.setOnClickListener { action.invoke() }
    }
}
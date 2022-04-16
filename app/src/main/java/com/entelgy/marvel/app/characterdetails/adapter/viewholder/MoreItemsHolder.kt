package com.entelgy.marvel.app.characterdetails.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.entelgy.marvel.R
import com.entelgy.marvel.databinding.LayoutMoreItemsAvailableBinding

class MoreItemsHolder(view: View, private val action : () -> Unit) : RecyclerView.ViewHolder(view) {
        private val binding = LayoutMoreItemsAvailableBinding.bind(view)

        fun bind(numberOfItems: Int) {
            binding.tvMoreItems.text = itemView.context.getString(R.string.more_items_available, numberOfItems)

            binding.root.setOnClickListener { action.invoke() }
        }
    }
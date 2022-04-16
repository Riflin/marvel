package com.entelgy.marvel.app.characterdetails.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.entelgy.marvel.R
import com.entelgy.marvel.app.callbacks.EventCallback
import com.entelgy.marvel.app.characterdetails.adapter.viewholder.MoreItemsHolder
import com.entelgy.marvel.app.utils.Utils
import com.entelgy.marvel.data.model.EventSummary
import com.entelgy.marvel.data.utils.Constants
import com.entelgy.marvel.databinding.ItemSummaryBinding

class EventSummaryAdapter(context: Context, private val events: List<EventSummary>, private val totalEvents: Int,
                          private val callback: EventCallback): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Constants.TYPE_ITEM -> {
                val view = inflater.inflate(R.layout.item_summary, parent, false)
                return EventHolder(view)
            }
            Constants.TYPE_MORE_ITEMS -> {
                val view = inflater.inflate(R.layout.layout_more_items_available, parent, false)
                MoreItemsHolder(view) { callback.onMoreEventSelected() }
            }
            else -> {
                val view = inflater.inflate(R.layout.item_summary, parent, false)
                return EventHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is EventHolder -> {
                if (position % 2 == 0) {
                    holder.itemView.setBackgroundResource(R.drawable.item_character_background)
                } else {
                    holder.itemView.setBackgroundResource(R.drawable.item_character_blue_background)
                }
                val event = events[position]

                holder.bind(event)
            }
            is MoreItemsHolder -> {
                holder.bind(totalEvents)
            }
        }
    }

    override fun getItemCount(): Int {
        return Utils.getItemCount(events, totalEvents)
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position < events.size -> Constants.TYPE_ITEM
            else -> Constants.TYPE_MORE_ITEMS
        }
    }

    inner class EventHolder(view: View): RecyclerView.ViewHolder(view) {
        private val binding = ItemSummaryBinding.bind(view)

        fun bind(event: EventSummary) {
            binding.tvName.text = event.name

            binding.root.setOnClickListener { callback.onEventSelected(event) }
        }
    }
}
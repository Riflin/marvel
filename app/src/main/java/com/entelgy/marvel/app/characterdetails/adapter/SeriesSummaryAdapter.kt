package com.entelgy.marvel.app.characterdetails.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.entelgy.marvel.R
import com.entelgy.marvel.app.callbacks.SeriesCallback
import com.entelgy.marvel.app.characterdetails.adapter.viewholder.MoreItemsHolder
import com.entelgy.marvel.app.utils.AppUtils
import com.entelgy.marvel.data.model.SeriesSummary
import com.entelgy.marvel.data.utils.Constants
import com.entelgy.marvel.databinding.ItemSummaryBinding

/**
 * Adapter para las series. Mismo funcionamiento que los otros
 */
class SeriesSummaryAdapter(context: Context, private val series: List<SeriesSummary>, private val totalSeries: Int,
                           private val callback: SeriesCallback): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            Constants.TYPE_ITEM -> {
                val view = inflater.inflate(R.layout.item_summary, parent, false)
                return SerieHolder(view)
            }
            Constants.TYPE_MORE_ITEMS -> {
                val view = inflater.inflate(R.layout.layout_more_items_available, parent, false)
                MoreItemsHolder(view) { callback.onMoreSeriesSelected() }
            }
            //NUNCA llegaremos
            else -> {
                val view = inflater.inflate(R.layout.item_summary, parent, false)
                SerieHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SerieHolder -> {
                if (position % 2 == 0) {
                    holder.itemView.setBackgroundResource(R.drawable.item_character_background)
                } else {
                    holder.itemView.setBackgroundResource(R.drawable.item_character_blue_background)
                }
                val serie = series[position]

                holder.bind(serie)
            }
            is MoreItemsHolder -> {
                holder.bind(totalSeries)
            }
        }
    }

    override fun getItemCount(): Int {
        return AppUtils.getItemCount(series, totalSeries)
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position < series.size -> Constants.TYPE_ITEM
            else -> Constants.TYPE_MORE_ITEMS
        }
    }

    inner class SerieHolder(view: View): RecyclerView.ViewHolder(view) {
        private val binding = ItemSummaryBinding.bind(view)

        fun bind(serie: SeriesSummary) {
            binding.tvName.text = serie.name

            binding.root.setOnClickListener { callback.onSeriesSelected(serie) }
        }
    }
}
package com.entelgy.marvel.app.characterdetails.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.entelgy.marvel.R
import com.entelgy.marvel.app.callbacks.StoriesCallback
import com.entelgy.marvel.app.characterdetails.adapter.viewholder.MoreItemsHolder
import com.entelgy.marvel.app.utils.AppUtils
import com.entelgy.marvel.data.model.StorySummary
import com.entelgy.marvel.data.utils.Constants
import com.entelgy.marvel.databinding.ItemSummaryBinding

/**
 * Adapter para las historias. Mismo funcionamieneo que el de comics, eventos y series
 */
class StorySummaryAdapter(context: Context, private val stories: List<StorySummary>, private val totalStories: Int,
                          private val callback: StoriesCallback): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Constants.TYPE_ITEM -> {
                val view = inflater.inflate(R.layout.item_summary, parent, false)
                StoryHolder(view)
            }
            Constants.TYPE_MORE_ITEMS -> {
                val view = inflater.inflate(R.layout.layout_more_items_available, parent,  false)
                MoreItemsHolder(view) { callback.onMoreStoriesSelected() }
            }
            else -> {
                val view = inflater.inflate(R.layout.item_summary, parent, false)
                StoryHolder(view)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is StoryHolder -> {
                if (position % 2 == 0) {
                    holder.itemView.setBackgroundResource(R.drawable.item_character_background)
                } else {
                    holder.itemView.setBackgroundResource(R.drawable.item_character_blue_background)
                }
                val story = stories[position]

                holder.bind(story)
            }
            is MoreItemsHolder -> {
                holder.bind(totalStories)
            }
        }
    }

    override fun getItemCount(): Int {
        return AppUtils.getItemCount(stories, totalStories)
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position < stories.size -> Constants.TYPE_ITEM
            else -> Constants.TYPE_MORE_ITEMS
        }
    }

    inner class StoryHolder(view: View): RecyclerView.ViewHolder(view) {
        private val binding = ItemSummaryBinding.bind(view)

        fun bind(story: StorySummary) {
            binding.tvName.text = story.name

            binding.root.setOnClickListener { callback.onStorySelected(story) }
        }
    }
}
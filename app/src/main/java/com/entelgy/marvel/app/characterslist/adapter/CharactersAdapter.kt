package com.entelgy.marvel.app.characterslist.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.entelgy.marvel.R
import com.entelgy.marvel.app.callbacks.CharactersCallback
import com.entelgy.marvel.app.callbacks.OnBottomReachedListener
import com.entelgy.marvel.app.utils.Utils
import com.entelgy.marvel.data.model.imageformats.PortraitImage
import com.entelgy.marvel.data.model.characters.Character
import com.entelgy.marvel.databinding.ItemCharacterBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class CharactersAdapter(private val context: Context, characters: List<Character>,
                        private val callback: CharactersCallback,
                        private val listener: OnBottomReachedListener
): RecyclerView.Adapter<CharactersAdapter.CharacterViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    private val charactersList = ArrayList(characters)

    private val picasso = Utils.getPicasso(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = inflater.inflate(R.layout.item_character, parent, false)
        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = charactersList[position]

        if (position == charactersList.size - 1) {
            listener.onBottomReached()
        }

        holder.bind(character)
    }

    override fun getItemCount(): Int {
        return charactersList.size
    }

    fun addCharacters(characters: List<Character>) {
        val size = charactersList.size
        charactersList.addAll(characters)
        notifyItemRangeInserted(size, characters.size)
    }

    inner class CharacterViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val binding = ItemCharacterBinding.bind(view)

        fun bind(character: Character) {
            //Mostramos la imagen, con un progress que se quitará al cargarse
            binding.imageProgress.visibility = View.VISIBLE

            val path = character.getThumbnailPath(PortraitImage.Xlarge)

            Picasso.get().load(path)
                .into(binding.ivThumbnail, object: Callback {
                    override fun onSuccess() {
                        binding.imageProgress.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        binding.ivThumbnail.setImageResource(R.drawable.ic_broken_image)
                        binding.imageProgress.visibility = View.GONE
                    }
                })

            //Mostramos el nombre
            binding.tvName.text = character.name

            //Mostramos la descripción
//            binding.tvDescription.text = character.description

            //Mostramos el número de cómics
//            binding.tvComics.text = character.commics?.available?.toString() ?: "0"

            binding.root.setOnClickListener { callback.onCharacterSelected(character) }
        }
    }
}
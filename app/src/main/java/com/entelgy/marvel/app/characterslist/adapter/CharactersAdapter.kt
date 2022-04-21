package com.entelgy.marvel.app.characterslist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.entelgy.marvel.R
import com.entelgy.marvel.app.callbacks.CharactersCallback
import com.entelgy.marvel.app.callbacks.OnBottomReachedListener
import com.entelgy.marvel.app.utils.AppUtils
import com.entelgy.marvel.data.model.imageformats.PortraitImage
import com.entelgy.marvel.data.model.characters.Character
import com.entelgy.marvel.data.model.utils.Sort
import com.entelgy.marvel.databinding.ItemCharacterBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

/**
 * En este adapter se muestran los personajes. Necesita recibir un callback para indicar que
 * hemos seleccionado un personaje y otro callback para indicar que hemos llegado al final de la lista
 */
class CharactersAdapter(context: Context, characters: List<Character>,
                        private val callback: CharactersCallback,
                        private val listener: OnBottomReachedListener
): RecyclerView.Adapter<CharactersAdapter.CharacterViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    private var charactersList = ArrayList(characters)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = inflater.inflate(R.layout.item_character, parent, false)
        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = charactersList[position]

        //Si estamos al final de la lista, avisamos al listener para que haga lo que tenga que hacer (cargar más personajes)
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

    fun sortByName(sortName: Sort) {
        //Ordenamos la lista ascendente o descendemente por el nombre
        charactersList = when (sortName) {
            Sort.Ascending -> ArrayList(charactersList.sortedBy { it.name })
            Sort.Descending -> ArrayList(charactersList.sortedByDescending { it.name })
        }
        //Y notificamos los cambios al adapter para que refresque la vista
        notifyDataSetChanged()
    }

    fun sortByDate(sortDate: Sort) {
        //Ordenamos la lista ascendente o descendemente por el nombre
        charactersList = when (sortDate) {
            Sort.Ascending -> ArrayList(charactersList.sortedBy { it.modified })
            Sort.Descending -> ArrayList(charactersList.sortedByDescending { it.modified })
        }
        //Y notificamos los cambios al adapter para que refresque la vista
        notifyDataSetChanged()
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

            //Le advertimos al callback de que hemos seleccionado el personaje (en este caso, iremos a ver su detalle)
            binding.root.setOnClickListener { callback.onCharacterSelected(character) }
        }
    }
}
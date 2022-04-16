package com.entelgy.marvel.app.characterslist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.entelgy.marvel.R
import com.entelgy.marvel.app.callbacks.OnBottomReachedListener
import com.entelgy.marvel.app.characterslist.adapter.CharactersAdapter
import com.entelgy.marvel.app.characterslist.presenter.CharactersListPresenter
import com.entelgy.marvel.app.presenter.PresenterFactory
import com.entelgy.marvel.app.utils.base.BaseActivity
import com.entelgy.marvel.data.model.Sort
import com.entelgy.marvel.data.model.characters.Character
import com.entelgy.marvel.databinding.ActivityCharactersListBinding
import java.text.SimpleDateFormat
import java.util.*

class CharactersListActivity: BaseActivity(), CharactersListView, OnBottomReachedListener {


    companion object {
        fun createNewIntent(context: Context): Intent {
            return Intent(context, CharactersListActivity::class.java)
        }
    }
    private lateinit var binding: ActivityCharactersListBinding

    private lateinit var presenter: CharactersListPresenter

    private var adapter: CharactersAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCharactersListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Obtenemos los personajes de primeras
        presenter.getDataFromServer()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun init() {
        presenter = PresenterFactory.getCharactersListPresenter()
        presenter.view = this
        presenter.create()
    }

    override fun initViews() {
        binding.toolbar.title = getString(R.string.personajes_marvel)
        binding.swRefresh.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary),
            ContextCompat.getColor(this, R.color.colorPrimaryDark), ContextCompat.getColor(this, R.color.dark_green),
            ContextCompat.getColor(this, R.color.purple_200), ContextCompat.getColor(this, R.color.green))
        binding.swEmptyRefresh.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary),
            ContextCompat.getColor(this, R.color.colorPrimaryDark), ContextCompat.getColor(this, R.color.dark_green),
            ContextCompat.getColor(this, R.color.purple_200), ContextCompat.getColor(this, R.color.green))

        val layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        binding.rvCharacters.layoutManager = layoutManager
    }

    override fun attachListenersToTheViews() {
        binding.tvFiltroNombre.setOnClickListener { presenter.selectNameFilter() }
        binding.tvFiltroFecha.setOnClickListener { presenter.selectDateFilter() }
        binding.tvSortName.setOnClickListener { presenter.sortByName() }
        binding.tvSortDate.setOnClickListener { presenter.sortByDate() }
        binding.tvBuscar.setOnClickListener { presenter.getDataFromServer() }
        binding.ivBorrarNombre.setOnClickListener { presenter.resetNameFilter() }
        binding.ivBorrarFecha.setOnClickListener { presenter.resetDateFilter() }
        binding.ivBorrarSortName.setOnClickListener { presenter.resetSortByName() }
        binding.ivBorrarSortDate.setOnClickListener { presenter.resetSortByDate() }

        binding.tvVerFiltros.setOnClickListener { toggleFiltrosVisibility() }

        binding.swRefresh.setOnRefreshListener { presenter.getDataFromServer() }
        binding.swEmptyRefresh.setOnRefreshListener { presenter.getDataFromServer() }
    }

    /**
     * Con esta función mostramos u ocultamos los filtros disponibles.
     * La dejamos aquí directamente en la vista, sin pasar por el presenter, porque es un tema
     * exclusivamente de la vista, sin datos de por medio
     */
    private fun toggleFiltrosVisibility() {
        if (binding.clFiltros.visibility == View.VISIBLE) {
            binding.clFiltros.visibility = View.GONE
            binding.tvVerFiltros.text = getString(R.string.ver_filtros)
        } else {
            binding.clFiltros.visibility = View.VISIBLE
            binding.tvVerFiltros.text = getString(R.string.ocultar_filtros)
        }
    }

    override fun showLoading(show: Boolean) {
        binding.swRefresh.isRefreshing = show
    }

    override fun onFilterNameSelected(filter: String) {
        //Ponemos el nombre buscado en el botón
        binding.tvFiltroNombre.text = filter
        //Y lo seleccionamos, para que se vea en verde
        binding.tvFiltroNombre.isSelected = true
        //Mostramos el hint
        binding.tvHintNombre.visibility = View.VISIBLE
    }

    override fun resetFilterName() {
        //Volvemos a ponerle el texto original
        binding.tvFiltroNombre.text = getString(R.string.nombre)
        //Y lo deseleccionamos para que se muestre de nuevo como era
        binding.tvFiltroNombre.isSelected = false
        //Ocultamos también el hint
        binding.tvHintNombre.visibility = View.GONE
    }

    override fun showDeleteFilterName(show: Boolean) {
        //Mostramos el botón de borrar el nombre en función del parámetro
        binding.ivBorrarNombre.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun onFilterDateSelected(date: Date) {
        //Mostramos la fecha elegida
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        binding.tvFiltroFecha.text = sdf.format(date)
        binding.tvFiltroFecha.isSelected = true
        binding.tvHintFecha.visibility = View.VISIBLE
    }

    override fun resetFilterDate() {
        binding.tvHintFecha.visibility = View.GONE
        binding.tvFiltroFecha.text = getString(R.string.fecha_modificacion)
        binding.tvFiltroFecha.isSelected = false
    }

    override fun showDeleteFilterDate(show: Boolean) {
        binding.ivBorrarFecha.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun onSortNameSelected(sort: Sort) {
        when (sort) {
            Sort.Ascending -> {
                binding.tvSortName.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up, 0)
            }
            Sort.Descending -> {
                binding.tvSortName.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0)
            }
        }
        binding.tvSortName.isSelected = true
    }

    override fun showDeleteSortName(show: Boolean) {
        binding.ivBorrarSortName.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun onSortDateSelected(sort: Sort) {
        when (sort) {
            Sort.Ascending -> {
                binding.tvSortDate.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up, 0)
            }
            Sort.Descending -> {
                binding.tvSortDate.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0)
            }
        }
        binding.tvSortDate.isSelected = true
    }

    override fun showDeleteSortDate(show: Boolean) {
        binding.ivBorrarSortDate.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun resetSortName() {
        binding.tvSortName.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0,0,0)
        binding.tvSortName.isSelected = false
    }

    override fun resetSortDate() {
        binding.tvSortDate.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0,0,0)
        binding.tvSortDate.isSelected = false
    }

    override fun showCharacters(characters: List<Character>) {
        if (characters.isNotEmpty()) {
            adapter = CharactersAdapter(this, characters, presenter, this)
            binding.rvCharacters.adapter = adapter
            binding.swRefresh.visibility = View.VISIBLE
            binding.swEmptyRefresh.visibility = View.GONE
        } else {
            binding.swRefresh.visibility = View.GONE
            binding.swEmptyRefresh.visibility = View.VISIBLE
        }
    }

    override fun addCharacters(characters: List<Character>) {
        adapter?.addCharacters(characters)
    }

    override fun onBottomReached() {
        presenter.getMoreCharacters()
    }

    override fun showCopyright(copyright: String?) {
        binding.tvCopyright.text = copyright ?: getString(R.string.copyright)
    }
}
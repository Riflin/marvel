package com.entelgy.marvel.app.characterslist

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
import com.entelgy.marvel.data.model.utils.Sort
import com.entelgy.marvel.data.model.characters.Character
import com.entelgy.marvel.databinding.ActivityCharactersListBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * Pantalla inicial de la aplicación, donde mostramos una lista con los personajes de Marvel
 */
class CharactersListActivity: BaseActivity(), CharactersListView, OnBottomReachedListener {


    private lateinit var binding: ActivityCharactersListBinding

    private lateinit var presenter: CharactersListPresenter<CharactersListView>

    private var adapter: CharactersAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Inflamos la vista de la actividad
        binding = ActivityCharactersListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Obtenemos los personajes de primeras
        presenter.getDataFromServer()
    }

    /**
     * Siempre llamamos al destroy() del presenter en cada activity, para limpiar la vista y que no haya fugas de memoria
     */
    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun init() {
        //Inicializamos el presenter
        presenter = PresenterFactory.getCharactersListPresenter()
        presenter.view = this
        presenter.create()
    }

    override fun initViews() {
        //Inicializamos las vistas
        //Título de la activity
        binding.toolbar.title = getString(R.string.characters_list)
        //Colores del progress del swipeToRefresh
        binding.swRefresh.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary),
            ContextCompat.getColor(this, R.color.colorPrimaryDark), ContextCompat.getColor(this, R.color.dark_green),
            ContextCompat.getColor(this, R.color.purple_200), ContextCompat.getColor(this, R.color.green))
        binding.swEmptyRefresh.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary),
            ContextCompat.getColor(this, R.color.colorPrimaryDark), ContextCompat.getColor(this, R.color.dark_green),
            ContextCompat.getColor(this, R.color.purple_200), ContextCompat.getColor(this, R.color.green))

        //Mostraremos los personajes en un gridlayout de dos columnas
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
        binding.swEmptyRefresh.isRefreshing = show
    }

    /**
     * Muestra el filtro introducido por el usuario
     */
    override fun onFilterNameSelected(filter: String) {
        //Ponemos el nombre buscado en el botón
        binding.tvFiltroNombre.text = filter
        //Y lo seleccionamos, para que se vea en verde
        binding.tvFiltroNombre.isSelected = true
        //Mostramos el hint
        binding.tvHintNombre.visibility = View.VISIBLE
    }

    /**
     * Resetea el filtro por nombre
     */
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

    /**
     * Muestra la fecha elegida por el usuario
     */
    override fun onFilterDateSelected(date: Date) {
        //Mostramos la fecha elegida
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        binding.tvFiltroFecha.text = sdf.format(date)
        binding.tvFiltroFecha.isSelected = true
        binding.tvHintFecha.visibility = View.VISIBLE
    }

    /**
     * Resetea el filtro de la fecha
     */
    override fun resetFilterDate() {
        binding.tvHintFecha.visibility = View.GONE
        binding.tvFiltroFecha.text = getString(R.string.fecha_modificacion)
        binding.tvFiltroFecha.isSelected = false
    }

    override fun showDeleteFilterDate(show: Boolean) {
        binding.ivBorrarFecha.visibility = if (show) View.VISIBLE else View.GONE
    }

    /**
     * Muestar la ordenación por nombre escogida
     */
    override fun onSortNameSelected(sort: Sort) {
        when (sort) {
            Sort.Ascending -> {
                binding.tvSortName.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up, 0)
            }
            Sort.Descending -> {
                binding.tvSortName.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0)
            }
        }
        //Seleccionamos el textView para que se ponga con el color de seleccionado
        binding.tvSortName.isSelected = true
    }

    /**
     * Muestra el botón para eliminar la ordenación por nombre
     */
    override fun showDeleteSortName(show: Boolean) {
        binding.ivBorrarSortName.visibility = if (show) View.VISIBLE else View.GONE
    }

    /**
     * Ordenación por fecha escogida
     */
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

    /**
     * Elimina la ordenación por nombre
     */
    override fun resetSortName() {
        //Quitamos la fecha que indica la ordenación
        binding.tvSortName.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0,0,0)
        //Y deseleccionamos el textView, para que vuelva a su color original
        binding.tvSortName.isSelected = false
    }

    /**
     * Elimina la ordenación por fecha
     */
    override fun resetSortDate() {
        binding.tvSortDate.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0,0,0)
        binding.tvSortDate.isSelected = false
    }

    /**
     * Llama al adapter para que ordene la lista por nombre
     */
    override fun sortByName(sortName: Sort) {
        adapter?.sortByName(sortName)
    }

    /**
     * Ordena la lista por fecha
     */
    override fun sortByDate(sortName: Sort) {
        adapter?.sortByDate(sortName)
    }

    /**
     * Muestra el listado de personajes que tenemos. Si está vacío, muestra un emptyView para
     * que la pantalla no sea muy sosa.
     */
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

    /**
     * Añade los personajes indicados a la lista que ya mostraba el adapter
     */
    override fun addCharacters(characters: List<Character>) {
        adapter?.addCharacters(characters)
    }

    /**
     * Al llegar al final de la lista, llamamos al presenter para que descargue más personajes
     */
    override fun onBottomReached() {
        presenter.getMoreCharacters()
    }

    /**
     * Mostramos el copyright de Marvel. Si no tenemos nada que mostrar, mostramos el que pone
     * la documentación de la api
     */
    override fun showCopyright(copyright: String?) {
        binding.tvCopyright.text = copyright ?: getString(R.string.copyright)
    }
}
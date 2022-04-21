package com.entelgy.marvel.app.characterslist.presenter

import androidx.fragment.app.DialogFragment
import com.entelgy.marvel.R
import com.entelgy.marvel.app.callbacks.NameFilterCallback
import com.entelgy.marvel.app.characterslist.CharactersListView
import com.entelgy.marvel.app.characterslist.dialog.DialogFiltroNombre
import com.entelgy.marvel.app.routing.Routing
import com.entelgy.marvel.data.model.CharacterSummary
import com.entelgy.marvel.data.model.characters.Character
import com.entelgy.marvel.data.model.characters.CharacterDataContainer
import com.entelgy.marvel.data.model.utils.Sort
import com.entelgy.marvel.domain.usecases.network.characters.GetCharactersFromServer
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.CoroutineContext

/**
 * Este presenter está parametrizado y es open para que pueda heredar de él el presenter de la
 * pantalla de los personajes por cómic
 */
open class CharactersListPresenterImpl<T: CharactersListView> : CharactersListPresenter<T>,
    NameFilterCallback, CoroutineScope {

    /* La vista aquí está parametrizada para poder tener otra vista (la de los personajes por cómic) en
     * el presenter que hereda de éste
     */
    override var view: T? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private val job = Job()

    //Ordenaciones
    protected var sortName: Sort? = null
    protected var sortDate: Sort? = null

    //Filtros
    protected var filterName: String? = null
    protected var filterDate: Date? = null

    //Aquí guardamos la última información descargada, para tener en cuenta lo que llevamos descargado
    protected var lastDataFetched: CharacterDataContainer? = null

    override fun create() {
        sortName = null
        sortDate = null
        filterName = null
        filterDate = null
        lastDataFetched = null
    }

    override fun resume() {

    }

    override fun pause() {

    }

    override fun stop() {

    }

    override fun destroy() {
        view = null
    }

    /**
     * Abre el diálogo para seleccionar el nombre
     */
    override fun selectNameFilter() {
        if (view != null) {
            val dialog = DialogFiltroNombre.createNewInstance(filterName, this)
            dialog.show(view!!.getSupportFragmentManager(), "NAME")
        }
    }

    /**
     * Hemos elegido un nombre para filtrar
     */
    override fun onNameFilterSelected(name: String?) {

        //Ocultamos el diálogo
        val dialog = view?.getSupportFragmentManager()?.findFragmentByTag("NAME") as? DialogFragment
        dialog?.dismiss()

        //Quitamos cualquier espacio que hubiera podido quedarse
        filterName = name?.trim()

        //Si no hemos escrito nada, reseteamos
        if (name.isNullOrBlank()) {
            resetNameFilter()
        } else {
            //Si no, lo mostramos
            view?.onFilterNameSelected(name)
            view?.showDeleteFilterName()
        }
    }

    override fun resetNameFilter() {
        filterName = null
        view?.resetFilterName()
        view?.showDeleteFilterName(show = false)
    }

    /**
     * Muestra un datePicker para seleccionar una fecha con la que filtrar los resultados
     */
    override fun selectDateFilter() {
        if (view != null) {
            val calendar = Calendar.getInstance()
            if (filterDate != null) {
                calendar.time = filterDate!!
            }
            val datePicker = DatePickerDialog.newInstance(
                FechaListener(), calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
            )
            //No podremos seleccionar una fecha posterior a hoy
            datePicker.maxDate = Calendar.getInstance()

            datePicker.show(view!!.getSupportFragmentManager(), "DATE")
        }
    }

    override fun resetDateFilter() {
        filterDate = null
        view?.resetFilterDate()
        view?.showDeleteFilterDate(show = false)
    }


    /**
     * En función de la ordenación que tuviéramos seleccionada, selecciona una u otra
     */
    override fun sortByName() {
        sortName = when (sortName) {
            Sort.Ascending -> Sort.Descending
            Sort.Descending -> Sort.Ascending
            null -> Sort.Ascending
        }
        //Sólo podemos  tener una ordenación a la vez, así que borramos la de fecha
        sortDate = null
        //Lo mostramos en la vista, borrando la otra ordenación si la tuviéramos
        view?.onSortNameSelected(sortName!!)
        view?.showDeleteSortName()
        view?.resetSortDate()
        view?.showDeleteSortDate(show = false)
        view?.sortByName(sortName!!)
    }

    override fun resetSortByName() {
        sortName = null
        view?.resetSortName()
        view?.showDeleteSortName(show = false)
    }

    /**
     * En función de la ordenación que tuviéramos seleccionada, selecciona una u otra
     */
    override fun sortByDate() {
        sortDate = when (sortDate) {
            Sort.Ascending -> Sort.Descending
            Sort.Descending -> Sort.Ascending
            //Por defecto, ascendente
            null -> Sort.Ascending
        }
        //Sólo podemos  tener una ordenación a la vez, así que borramos la de nombre
        sortName = null
        //Lo mostramos en la vista
        view?.onSortDateSelected(sortDate!!)
        view?.showDeleteSortDate()
        view?.resetSortName()
        view?.showDeleteSortName(show = false)
        view?.sortByDate(sortDate!!)
    }

    override fun resetSortByDate() {
        sortDate = null
        view?.resetSortDate()
        view?.showDeleteSortDate(show = false)
    }

    /**
     * Se descarga los datos del servidor
     */
    override fun getDataFromServer() {
        downloadCharacters()
    }

    private fun downloadCharacters(loadMoreCharacters: Boolean = false) {
        launch {
            view?.showLoading()
            val sort = getSortParameter()
            try {
                /*Si vamos a descargar más personajes de los que ya teníamos, debemos utilizar los datos
                 * que ya teníamos para usar el offset y no volver a descargar los mismos personajes
                 * que ya teníamos
                 */
                val offset = if (loadMoreCharacters) {
                    (lastDataFetched?.offset ?: 0).plus(lastDataFetched?.count ?: 0)
                } else {
                    0
                }
                //Obtenemos los personajes con los filtros seleccionados
                val result = GetCharactersFromServer(filterName, filterDate, sort, offset).downloadData()

                //Comprobamos que la llamada ha sido correcta
                if (result.isSuccessful) {
                    view?.showLoading(false)
                    //Nos queadmos con CharacterDataContainer como último dato obtenido
                    val dataWrapper = result.body()
                    lastDataFetched = dataWrapper?.data
                    //Comprobamos que tenemos datos de verdad
                    lastDataFetched?.let {
                        //Mostramos el copyright
                        view?.showCopyright(dataWrapper!!.attributionText)
                        //Si estamos descargando más personajes, los añadimos
                        if (loadMoreCharacters) {
                            view?.addCharacters(it.results)
                        } else {
                            //Si no, los mostramos desde 0
                            view?.showCharacters(it.results)
                        }
                    }
                } else {
                    //Si ha fallado la llamada, mostramos el error
                    view?.showLoading(false)
                    val errorBody = result.errorBody()?.string()
                    if (errorBody != null) {
                        val json = JsonParser.parseString(errorBody)
                        if (json is JsonObject) {
                            val status = json.get("status").asString
                            view?.showError(status)
                        } else {
                            view?.showError(view?.viewContext?.getString(R.string.error_downloading_characters) ?: "")
                        }
                    } else {
                        view?.showError(view?.viewContext?.getString(R.string.error_downloading_characters) ?: "")
                    }
                }
            } catch (e: Exception) {
                //Cualquier excepción que diera la llamada, pues mostramos el error
                view?.showLoading(false)
                view?.showError(e.message ?: view?.viewContext?.getString(R.string.error_downloading_characters) ?: "")
            }
        }
    }

    /**
     * Obtiene el parámetro a enviar al servidor en función de la ordenación escogida por el usuario
     */
    protected fun getSortParameter(): String {
        val sort = when (sortDate) {
            Sort.Ascending -> "date"
            Sort.Descending -> "-date"
            null -> {
                when (sortName) {
                    Sort.Ascending -> "name"
                    Sort.Descending -> "-name"
                    null -> ""
                }
            }
        }
        return sort
    }

    /**
     * Obtiene más personajes a partir de los que ya teníamos descargados
     */
    override fun getMoreCharacters() {
        //Sólo descargamos más personajes si tenemos algo más que descargar
        lastDataFetched?.let { lastDataFetched ->
            /* Tendremos algo más que descargar si el total de personajes es mayor al count (número de
             * personajes obtenidos en la llamada) sumado al offset (número de personajes "saltados") de
             * ese ultimo DataContainer
             */
            if (lastDataFetched.total ?: 0 > (lastDataFetched.count ?: 0).plus(lastDataFetched.offset ?: 0)) {
                downloadCharacters(loadMoreCharacters = true)
            }
        } ?: run {
            //Realmente aquí nunca llegaremos, porque para cargar más datos debemos haber descargado antes
            downloadCharacters(loadMoreCharacters = true)
        }
    }

    /**
     * Mostramos los detalles del personaje
     */
    override fun onCharacterSelected(character: Character) {
        view?.viewContext?.let { context ->
            Routing.goToCharacterDetailsActivity(context, character)
        }
    }

    /**
     * En esta pantalla tenemos personajes (Character), por lo que este método del callback no tenemos
     * que implementarlo
     */
    override fun onCharacterSelected(character: CharacterSummary) {
        //NOTHING HERE
    }

    /**
     * Este método del callback tampoco es necesario implementarlo aquí
     */
    override fun onMoreCharactersSelected() {
        //NOTHING HERE
    }

    /**
     * Listener para el filtro de la fecha
     */
    private inner class FechaListener : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(
            dialog: DatePickerDialog?,
            year: Int,
            monthOfYear: Int,
            dayOfMonth: Int
        ) {
            //Aunque luego sólo enviamos día, mes y año, ponemos la fecha a las 00:00:00 del día elegido
            val calendar = Calendar.getInstance()
            calendar[Calendar.YEAR] = year
            calendar[Calendar.MONTH] = monthOfYear
            calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
            calendar[Calendar.HOUR_OF_DAY] = 0
            calendar[Calendar.MINUTE] = 0
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0

            filterDate = calendar.time

            //Lo mostramos en la vista
            view?.onFilterDateSelected(calendar.time)
            view?.showDeleteFilterDate()
        }
    }
}
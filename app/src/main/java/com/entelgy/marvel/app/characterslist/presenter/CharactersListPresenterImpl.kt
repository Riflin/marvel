package com.entelgy.marvel.app.characterslist.presenter

import androidx.fragment.app.DialogFragment
import com.entelgy.marvel.R
import com.entelgy.marvel.app.characterdetails.CharacterDetailsActivity
import com.entelgy.marvel.app.callbacks.NameFilterCallback
import com.entelgy.marvel.app.characterslist.CharactersListView
import com.entelgy.marvel.app.characterslist.dialog.DialogFiltroNombre
import com.entelgy.marvel.app.routing.Routing
import com.entelgy.marvel.data.model.CharacterSummary
import com.entelgy.marvel.data.model.utils.Sort
import com.entelgy.marvel.data.model.characters.Character
import com.entelgy.marvel.data.model.characters.CharacterDataContainer
import com.entelgy.marvel.domain.usecases.network.characters.GetCharactersFromServer
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext

class CharactersListPresenterImpl() : CharactersListPresenter,
    NameFilterCallback, CoroutineScope {

    override var view: CharactersListView? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private val job = Job()

    private var sortName: Sort? = null
    private var sortDate: Sort? = null

    private var filterName: String? = null
    private var filterDate: Date? = null


    private var lastDataFetched: CharacterDataContainer? = null

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

    override fun selectNameFilter() {
        if (view != null) {
            val dialog = DialogFiltroNombre.createNewInstance(filterName, this)
            dialog.show(view!!.getSupportFragmentManager(), "NAME")
        }
    }

    override fun onNameFilterSelected(name: String?) {

        val dialog = view?.getSupportFragmentManager()?.findFragmentByTag("NAME") as? DialogFragment
        dialog?.dismiss()

        //Quitamos cualquier espacio que hubiera podido quedarse
        filterName = name?.trim()

        if (name != null) {
            view?.onFilterNameSelected(name)
            view?.showDeleteFilterName()
        } else {
            resetNameFilter()
        }
    }

    override fun resetNameFilter() {
        filterName = null
        view?.resetFilterName()
        view?.showDeleteFilterName(show = false)
    }

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

    override fun sortByName() {
        sortName = when (sortName) {
            Sort.Ascending -> Sort.Descending
            Sort.Descending -> Sort.Ascending
            null -> Sort.Ascending
        }
        sortDate = null
        view?.onSortNameSelected(sortName!!)
        view?.showDeleteSortName()
        view?.resetSortDate()
        view?.showDeleteSortDate(show = false)
    }

    override fun resetSortByName() {
        sortName = null
        view?.resetSortName()
        view?.showDeleteSortName(show = false)
    }

    override fun sortByDate() {
        sortDate = when (sortDate) {
            Sort.Ascending -> Sort.Descending
            Sort.Descending -> Sort.Ascending
            null -> Sort.Ascending
        }
        sortName = null
        view?.onSortDateSelected(sortDate!!)
        view?.showDeleteSortDate()
        view?.resetSortName()
        view?.showDeleteSortName(show = false)
    }

    override fun resetSortByDate() {
        sortDate = null
        view?.resetSortDate()
        view?.showDeleteSortDate(show = false)
    }

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
                    view?.showError(result.errorBody()?.string() ?: view?.context?.getString(R.string.error_downloading_characters) ?: "")
                }
            } catch (e: Exception) {
                view?.showLoading(false)
                view?.showError(e.message ?: view?.context?.getString(R.string.error_downloading_characters) ?: "")
            }
        }
    }

    private fun getSortParameter(): String {
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

    override fun getMoreCharacters() {
        //Sólo descargamos más personajes si tenemos algo más que descargar
        lastDataFetched?.let { lastDataFetched ->
            if (lastDataFetched.total ?: 0 > lastDataFetched.count ?: 0) {
                downloadCharacters(loadMoreCharacters = true)
            }
        } ?: run {
            downloadCharacters(loadMoreCharacters = true)
        }
    }

    override fun onCharacterSelected(character: Character) {
        view?.context?.let { context ->
            Routing.goToCharacterDetailsActivity(context, character)
        }
    }

    override fun onCharacterSelected(character: CharacterSummary) {
        //NOTHING HERE
    }

    override fun onMoreCharactersSelected() {
        //NOTHING HERE
    }

    private inner class FechaListener : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(
            dialog: DatePickerDialog?,
            year: Int,
            monthOfYear: Int,
            dayOfMonth: Int
        ) {
            val calendar = Calendar.getInstance()
            calendar[Calendar.YEAR] = year
            calendar[Calendar.MONTH] = monthOfYear
            calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
            calendar[Calendar.HOUR_OF_DAY] = 0
            calendar[Calendar.MINUTE] = 0
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0

            filterDate = calendar.time

            view?.onFilterDateSelected(calendar.time)
            view?.showDeleteFilterDate()
        }
    }
}
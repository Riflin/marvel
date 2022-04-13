package com.entelgy.marvel.app.characterslist.presenter

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.entelgy.marvel.app.callbacks.NameFilterCallback
import com.entelgy.marvel.app.characterslist.CharactersListView
import com.entelgy.marvel.app.characterslist.dialog.DialogFiltroNombre
import com.entelgy.marvel.app.utils.Utils
import com.entelgy.marvel.data.model.Sort
import com.entelgy.marvel.data.model.characters.Character
import com.entelgy.marvel.data.model.characters.CharacterDataContainer
import com.entelgy.marvel.data.model.characters.CharacterDataWrapper
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

    private var deferredCharacters: Deferred<CharacterDataWrapper>? = null

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
        //Si aún estamos carganado una llamada al servidor, la cancelamos
        if (deferredCharacters?.isActive == true) {
            deferredCharacters?.cancel()
        }
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

        filterName = name

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
        launch {
            view?.showLoading()
//            val async1 = async(Dispatchers.IO) {
                val sort = when(sortDate) {
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
            try {
                val result = GetCharactersFromServer(filterName, filterDate, sort).downloadData()
//            }

//            val result = async1
                if (result.isSuccessful) {
                    Log.i("CHARACTERS", "CALL SUCCESS: ${result.body()}")
                    view?.hideLoading()
                    val characterDataWrapper = result.body()?.data
                    if (characterDataWrapper != null) {
                        view?.showCharacters(characterDataWrapper.results)
                    }
                } else {
                    Log.w("CHARACTERS", "CALL FAILED: ${result.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Utils.showDialogInformacion(view!!.getSupportFragmentManager(), "ATENCION", e.message)
            }
        }
//        Toast.makeText(view?.context, "NOT YET IMPLEMENTED", Toast.LENGTH_LONG).show()
//        Handler().postDelayed({view?.hideLoading()}, 3000)
    }

    override fun getMoreCharacters() {
        Toast.makeText(view?.context, "NOT YET IMPLEMENTED", Toast.LENGTH_LONG).show()
    }

    override fun onCharacterSelected(character: Character) {
        Toast.makeText(view?.context, "NOT YET IMPLEMENTED", Toast.LENGTH_LONG).show()
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
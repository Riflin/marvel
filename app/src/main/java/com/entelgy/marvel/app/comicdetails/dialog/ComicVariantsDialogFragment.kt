package com.entelgy.marvel.app.comicdetails.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.entelgy.marvel.R
import com.entelgy.marvel.app.characterdetails.adapter.ComicSummaryAdapter
import com.entelgy.marvel.app.comicdetails.dialog.presenter.ComicVariantsPresenter
import com.entelgy.marvel.app.presenter.PresenterFactory
import com.entelgy.marvel.app.utils.AppUtils
import com.entelgy.marvel.app.utils.base.BaseDialogFragment
import com.entelgy.marvel.data.model.characters.ComicSummary
import com.entelgy.marvel.data.utils.Constants
import com.entelgy.marvel.databinding.DialogComicVariantsBinding

class ComicVariantsDialogFragment: BaseDialogFragment(), ComicVariantsView {

    companion object {
        fun creanteNewInstance(variants: List<ComicSummary>): ComicVariantsDialogFragment {
            val dialog = ComicVariantsDialogFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList(Constants.COMIC_VARIANTS, ArrayList(variants))
            dialog.arguments = bundle

            return dialog
        }
    }

    private lateinit var binding: DialogComicVariantsBinding

    private lateinit var presenter: ComicVariantsPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogComicVariantsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun init() {
        presenter = PresenterFactory.getComicVariantsPresenter()
        presenter.view = this
    }

    override fun initViews(view: View) {
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvVariants.layoutManager = layoutManager
    }

    override fun attachListenersToTheViews() {
        //NOTHING HERE
    }

    override fun showData() {
        presenter.getData(arguments)
    }

    override fun showError(message: String) {
        AppUtils.showDialogInformacion(parentFragmentManager, getString(R.string.error), message)
    }

    override fun onDataError() {
        AppUtils.showDialogInformacion(parentFragmentManager, getString(R.string.error),
            getString(R.string.error_obteniendo_datos)) { dismissAllowingStateLoss() }
    }

    override fun getSupportFragmentManager(): FragmentManager {
        return parentFragmentManager
    }

    override fun showVariants(variants: List<ComicSummary>) {
        val adapter = ComicSummaryAdapter(requireContext(), variants, 0, presenter)
        binding.rvVariants.adapter = adapter
    }

    override fun onComicNotSelectable() {
        AppUtils.showDialogInformacion(parentFragmentManager, getString(R.string.error),
            getString(R.string.error_comic_without_id))
    }

    override fun showLoading(show: Boolean) {
        binding.progressVariants.visibility = if (show) View.VISIBLE else View.GONE
    }

    override val viewContext: Context
        get() = requireContext()
}
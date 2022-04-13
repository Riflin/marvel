package com.entelgy.marvel.app.characterslist.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.entelgy.marvel.R
import com.entelgy.marvel.app.callbacks.NameFilterCallback
import com.entelgy.marvel.app.utils.base.BaseDialogFragment
import com.entelgy.marvel.databinding.DialogFiltroNombreBinding

/**
 * En este diálogo introduciremos el nombre por el que queremos que empiecen los personajes a buscar
 */
class DialogFiltroNombre: BaseDialogFragment() {

    companion object {
        private const val TEXTO = "texto"

        fun createNewInstance(filtro: String?, callback: NameFilterCallback): DialogFiltroNombre {
            val dialog = DialogFiltroNombre()
            if (filtro != null) {
                val arguments = Bundle()
                arguments.putString(TEXTO, filtro)
                dialog.arguments = arguments
            }

            dialog.callback = callback

            return dialog
        }
    }

    private var _binding: DialogFiltroNombreBinding? = null
    private val binding get() = _binding!!

    private var texto: String? = null

    private lateinit var callback: NameFilterCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFiltroNombreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun init() {
        texto = arguments?.getString(TEXTO)

        if (!this::callback.isInitialized) {
            Toast.makeText(requireContext(), getString(R.string.error_instanciando_dialogo), Toast.LENGTH_LONG).show()
            dismissAllowingStateLoss()
        }
    }

    override fun initViews(view: View) {
        //NOTHING HERE
    }

    override fun attachListenersToTheViews() {
        binding.tvAceptar.setOnClickListener {
            callback.onNameFilterSelected(binding.etFiltro.text?.toString())
        }
    }

    override fun showData() {
        if (texto != null) {
            binding.etFiltro.setText(texto)
        }
    }
}
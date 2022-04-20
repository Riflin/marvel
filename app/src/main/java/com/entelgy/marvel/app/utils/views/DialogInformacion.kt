package com.entelgy.marvel.app.utils.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.entelgy.marvel.R
import com.entelgy.marvel.databinding.DialogInformacionBinding

class DialogInformacion: DialogFragment() {


    private lateinit var binding: DialogInformacionBinding


    private var title: String? = null
    private var texto: String? = null
    private var listener: View.OnClickListener? = null

    companion object {
        private const val TITLE = "title"
        private const val TEXTO = "texto"

        fun createNewInstance(
            title: String?,
            texto: String?,
            listener: View.OnClickListener?
        ): DialogInformacion {
            val dialogInformacion = DialogInformacion()
            val bundle = Bundle()
            bundle.putString(TITLE, title)
            bundle.putString(TEXTO, texto)
            dialogInformacion.arguments = bundle
            dialogInformacion.setListener(listener)
            return dialogInformacion
        }

        fun createNewInstance(
            title: String?,
            texto: String?,
            listener: () -> Unit
        ): DialogInformacion {
            val dialogInformacion = DialogInformacion()
            val bundle = Bundle()
            bundle.putString(TITLE, title)
            bundle.putString(TEXTO, texto)
            dialogInformacion.arguments = bundle
            dialogInformacion.listener = View.OnClickListener {
                listener.invoke()
            }
            return dialogInformacion
        }
    }


    private fun setListener(listener: View.OnClickListener?) {
        this.listener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogStyle)
        title = if (arguments != null) requireArguments().getString(TITLE) else ""
        texto = if (arguments != null) requireArguments().getString(TEXTO) else ""
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogInformacionBinding.inflate(layoutInflater)

        if (dialog != null) {
            dialog!!.setCancelable(false)
            dialog!!.setCanceledOnTouchOutside(false)
        }
        if (title != null && title!!.isNotEmpty()) {
            binding.tvTitle.text = title
        }
        binding.tvTexto.text = texto
        if (listener != null) {
            binding.tvAceptar.setOnClickListener { v ->
                listener!!.onClick(v)
                dismiss()
            }
        } else {
            binding.tvAceptar.setOnClickListener { dismiss() }
        }
        return binding.root
    }
}
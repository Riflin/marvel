package com.entelgy.marvel.app.utils.views

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.entelgy.marvel.R

class CustomProgressBar(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : LinearLayout(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    private val root: LinearLayout
    private val tvTexto: TextView
    private val progressBar: ProgressBar

    private var texto: String? = null
    private var colorText: Int = 0
    private var textSize: Int = 0
    private var textMarginTop: Int = 0
    @DrawableRes
    private var background: Int = 0
    @DrawableRes
    private var drawable: Int = 0

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar, 0, 0)

        texto = a.getString(R.styleable.CustomProgressBar_progress_text)
        colorText = a.getColor(R.styleable.CustomProgressBar_progress_textColor, ContextCompat.getColor(context, R.color.dark_grey))
        textSize = a.getDimensionPixelSize(R.styleable.CustomProgressBar_progress_textSize, 0)
        textMarginTop = a.getDimensionPixelSize(R.styleable.CustomProgressBar_progress_textMarginTop, 0)
        background = a.getResourceId(R.styleable.CustomProgressBar_progress_background, R.color.transparent)
        drawable = a.getResourceId(R.styleable.CustomProgressBar_progress_drawable, R.drawable.custom_progress)
        val textVisible = a.getBoolean(R.styleable.CustomProgressBar_progress_textVisible, false)

        a.recycle()

        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.custom_progress_bar, this, true)

        root = findViewById(R.id.root)
        tvTexto = findViewById(R.id.tvTexto)
        progressBar = findViewById(R.id.customProgress)

        tvTexto.visibility = if (textVisible) View.VISIBLE else View.GONE
        tvTexto.text = texto
        tvTexto.setTextColor(colorText)
        if (textSize > 0) {
            tvTexto.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
        }
        val params: LayoutParams = tvTexto.layoutParams as LayoutParams
        params.topMargin = textMarginTop
        tvTexto.layoutParams = params

        progressBar.indeterminateDrawable = ContextCompat.getDrawable(context, drawable)
        progressBar.progressDrawable = ContextCompat.getDrawable(context, drawable)

        root.setBackgroundResource(background)
    }

    fun setText(texto: String) {
        this.texto = texto
        tvTexto.text = texto
    }

    fun setTextSiza(size: Int) {
        this.textSize = size
        tvTexto.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
    }
}
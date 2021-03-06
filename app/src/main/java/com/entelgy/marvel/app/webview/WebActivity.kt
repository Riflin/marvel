package com.entelgy.marvel.app.webview

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.webkit.*
import com.entelgy.marvel.R
import com.entelgy.marvel.app.presenter.PresenterFactory
import com.entelgy.marvel.app.utils.AppUtils
import com.entelgy.marvel.app.utils.base.BaseActivity
import com.entelgy.marvel.app.webview.presenter.WebPresenter
import com.entelgy.marvel.data.model.Url
import com.entelgy.marvel.data.utils.Constants
import com.entelgy.marvel.databinding.ActivityWebviewBinding

/**
 * Activity con un webView para navegar por los enlaces de la aplicación sin tener que abrir
 * un navegador externo
 */
class WebActivity: BaseActivity(), WebView {

    companion object {
        /**
         * Debemos pasar la url a la que navegar
         */
        fun createNewIntent(context: Context, url: Url): Intent {
            return Intent(context, WebActivity::class.java).apply {
                putExtra(Constants.URL, url)
            }
        }
    }

    private lateinit var presenter: WebPresenter

    private lateinit var binding: ActivityWebviewBinding

    private var errorLoading: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter.getData(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun init() {
        presenter = PresenterFactory.getWebPresenter()
        presenter.view = this
        presenter.create()
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initViews() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        //Ajustes del webView
        binding.webView.settings.domStorageEnabled = true
        binding.webView.webViewClient = MarvelWebViewClient()
        binding.webView.webChromeClient = MarvelWebChromeClient()
        binding.webView.clearCache(false)
        binding.webView.settings.javaScriptCanOpenWindowsAutomatically = true
        binding.webView.settings.javaScriptEnabled = true
    }

    override fun attachListenersToTheViews() {
        //Si falla la carga, se muestra este botón con el que podemos reintentarla
        binding.tvRetry.setOnClickListener { presenter.getData(intent) }
    }

    override fun showLoading(show: Boolean) {
        if (show) {
            binding.clProgress.visibility = View.VISIBLE
            binding.tvRetry.visibility = View.GONE
        }
    }

    /**
     * Cargamos la url en el webview
     */
    override fun showWebpage(url: String) {
        binding.webView.loadUrl(url)
    }

    /**
     * Si la url no es válida, chapamos
     */
    override fun onUrlInvalid() {
        AppUtils.showDialogInformacion(supportFragmentManager, getString(R.string.error),
            getString(R.string.url_invalid)) { finish() }
    }

    /**
     * WebViewClient personalizado
     */
    private inner class MarvelWebViewClient: WebViewClient() {
        override fun onPageStarted(view: android.webkit.WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            errorLoading = false
            binding.tvRetry.visibility = View.GONE
        }

        @SuppressLint("WebViewClientOnReceivedSslError")
        override fun onReceivedSslError(view: android.webkit.WebView?, handler: SslErrorHandler?, error: SslError?) {
            val mensaje: String
            when (error?.primaryError) {
                SslError.SSL_UNTRUSTED -> {
                    mensaje = "SSL Untrusted"
                }
                SslError.SSL_NOTYETVALID -> {
                    mensaje = "SSL NOT YET VALID"
                }
                SslError.SSL_EXPIRED -> {
                    mensaje = "SSL EXPIRED"
                }
                SslError.SSL_IDMISMATCH -> {
                    mensaje = "SSL IDMISMATCH"
                }
                SslError.SSL_DATE_INVALID -> {
                    mensaje = "SSL DATE INVALID"
                }
                SslError.SSL_INVALID -> {
                    mensaje = "SSL INVALID"
                }
                else -> {
                    mensaje = "SSL DESCONOCIDO"
                }
            }
            Log.w("WEBVIEW", mensaje)
            handler?.proceed()
        }

        @Deprecated("Deprecated in Java")
        override fun shouldOverrideUrlLoading(view: android.webkit.WebView?, url: String): Boolean {
            view?.loadUrl(url)
            return true
        }

        override fun onReceivedError(view: android.webkit.WebView?, request: WebResourceRequest?, error: WebResourceError?) {
            super.onReceivedError(view, request, error)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.w("WEBVIEW", "ERROR LOADING ${request?.url}: ${error?.errorCode}")
            }
            errorLoading = true
            binding.tvRetry.visibility = View.VISIBLE
        }
    }

    /**
     * WebChromeClient personalizado para mostrar un progress "chulo" en el que se va rellenando
     * el logo de marvel conforme vamos cargando la página
     */
    private inner class MarvelWebChromeClient: WebChromeClient() {
        override fun onProgressChanged(view: android.webkit.WebView?, newProgress: Int) {
            Log.i("WEBVIEW", "PROGRESS: $newProgress")
            if (newProgress == 0) {
                binding.clProgress.visibility = View.VISIBLE
            }
            binding.ivProgress.setImageLevel(100*newProgress)
            if (newProgress == 100) {
                binding.clProgress.visibility = View.GONE
                if (!errorLoading) {
                    binding.tvRetry.visibility = View.GONE
                }
            }
        }
    }
}
package com.entelgy.marvel.data.services

import com.entelgy.marvel.data.utils.Constants
import com.entelgy.marvel.data.utils.DataUtils
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ServiceFactory {

    private const val TIMEOUT = 600L

    /**
     * Crea el objeto Retrofit con que hacer las llamadas
     */
    private fun getRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BASIC

        val parametersInterceptor = getParamsInterceptor()

        val client = OkHttpClient.Builder().addInterceptor(interceptor)
            .addInterceptor(parametersInterceptor)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build()


        return Retrofit.Builder().baseUrl(Constants.URL_BASE)
            .client(client)
            //Gson propio creado para obtener bien la información
            .addConverterFactory(GsonConverterFactory.create(DataUtils.getGson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    }

    /**
     * Con esto añadimos los parámetros necesarios a las llamadas para que funcione bien la api
     */
    private fun getParamsInterceptor(): Interceptor {
        val timestamp = System.currentTimeMillis()
        val hash = DataUtils.getHash(timestamp)

        /* Con este parameter añadimos siempre los tres parámetros que debemos pasar
         * a cualquier llamada a la api de marvel para que funcione bien */
        val parametersInterceptor = Interceptor { chain ->
            var request: Request = chain.request()
            val url: HttpUrl = request.url.newBuilder()
                .addQueryParameter("ts", timestamp.toString())
                .addQueryParameter("apikey", Constants.PUBLIC_KEY)
                .addQueryParameter("hash", hash)
                .build()
            request = request.newBuilder().url(url).build()
            chain.proceed(request)
        }
        return parametersInterceptor
    }

    //Obtiene el servicio con el que llamar para la info de los personajes
    fun getCharactersService(): CharactersService {
        return getRetrofit().create(CharactersService::class.java)
    }

    //Servicio para los cómics
    fun getComicsService(): ComicsService {
        return getRetrofit().create(ComicsService::class.java)
    }
}
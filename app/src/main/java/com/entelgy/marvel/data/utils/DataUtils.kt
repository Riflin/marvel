package com.entelgy.marvel.data.utils

import com.google.gson.*
import java.lang.reflect.Type
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object DataUtils {

    /**
     * Crea un Gson preparado para transformar las fechas devueltas por el servidor
     */
    fun getGson(): Gson {
        val builder = GsonBuilder()
        builder.registerTypeAdapter(Date::class.java, DateDeserializer())

        return builder.create()
    }

    private class DateDeserializer() : JsonDeserializer<Date?> {

        override fun deserialize(
            jsonElement: JsonElement, typeOF: Type?,
            context: JsonDeserializationContext?
        ): Date? {
            val format = "+yyyy-MM-dd'T'HH:mm:ssZ"
            try {
                return SimpleDateFormat(format, Locale.getDefault()).parse(jsonElement.asString)
            } catch (_: ParseException) {
            }
            return Date()
        }
    }

    /**
     * Obtiene el hash necesario para realizar las llamadas a la api de marvel
     */
    fun getHash(timestamp: Long): String {
        val key = timestamp.toString() + Constants.PRIVATE_KEY + Constants.PUBLIC_KEY
        return md5(key)
    }

    private fun md5(text: String?): String {
        if (text != null && text.isNotEmpty()) {
            val digester: MessageDigest
            try {
                digester = MessageDigest.getInstance("MD5")
                digester.update(text.toByteArray())
                val hash = digester.digest()
                val hexString = StringBuilder()
                for (aHash in hash) {
                    if (0xff and aHash.toInt() < 0x10) {
                        hexString.append("0").append(Integer.toHexString(0xFF and aHash.toInt()))
                    } else {
                        hexString.append(Integer.toHexString(0xFF and aHash.toInt()))
                    }
                }
                return hexString.toString()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
        }
        return ""
    }
}
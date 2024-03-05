package com.example.eventfinder.Model

import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class KeywordApi {
    fun autoComplete(input: String): ArrayList<String> {
        val arrayList = ArrayList<String>()
        var connection: HttpURLConnection? = null
        val jsonResult = StringBuilder()
        try {
            val sb = StringBuilder("https://assignment8-377801.wl.r.appspot.com/suggest?")
            sb.append("keyword=$input")
            val url = URL(sb.toString())
            connection = url.openConnection() as HttpURLConnection
            val inputStreamReader = InputStreamReader(
                connection!!.inputStream
            )
            var read: Int
            val buffer = CharArray(1024)
            while (inputStreamReader.read(buffer).also { read = it } != -1) {
                jsonResult.append(buffer, 0, read)
            }
        } catch (e: MalformedURLException) {
            throw RuntimeException(e)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            connection?.disconnect()
        }
        try {
            val jsonobject = JSONObject(jsonResult.toString())
            val data = jsonobject.getJSONObject("_embedded")
            val prediction = data.getJSONArray("attractions")
            Log.e("prodicaition", prediction.length().toString())
            for (i in 0 until prediction.length()) {
                arrayList.add(prediction.getJSONObject(i).getString("name"))
            }
        } catch (e: JSONException) {
            throw RuntimeException(e)
        }
        return arrayList
    }
}
package com.example.eventfinder.Activity

import android.content.Context
import android.content.SharedPreferences
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventfinder.Adapter.ResultsAdapter
import com.example.eventfinder.Adapter.ResultsAdapter.ItemClicks
import com.example.eventfinder.Client.RetrofitClient
import com.example.eventfinder.Listener.OnFragmentInteractionListener
import com.example.eventfinder.Model.Event
import com.example.eventfinder.R
import com.example.eventfinder.Util.EventSpinner
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class EventActivity constructor() : AppCompatActivity(), ItemClicks {
    private var events: MutableList<Event>? = null
    var gson: Gson? = null
    var mPrefs: SharedPreferences? = null
    var resultsAdapter: ResultsAdapter? = null
    private var eventSpinner: EventSpinner? = null
    private val mContext: Context? = null
    private val mListener: OnFragmentInteractionListener? = null
    private var recyclerView: RecyclerView? = null
    private var back: Button? = null
    private var noEvents: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)
        mPrefs = getSharedPreferences("pref", MODE_PRIVATE)
        gson = Gson()
        events = ArrayList()
        recyclerView = findViewById(R.id.recyclerView)
        noEvents = findViewById(R.id.no_events)
        getEvents()
        back = findViewById(R.id.back)
        back?.setBackgroundResource(0)
        back?.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                onBackPressed()
            }
        })
    }

    public override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun getEvents() {
        events!!.clear()
        showProgress(true)
        val preferences: SharedPreferences = getSharedPreferences("pref", MODE_PRIVATE)
        val data: MutableMap<String, String?> = HashMap()
        data.put("keyword", preferences.getString("keyword", "Taylor Swift"))
        data.put("dist", preferences.getString("distance", "10"))
        data.put("cat", preferences.getString("category", "Music"))
        data.put("LOC", preferences.getString("location", ""))
        if (preferences.getBoolean("locationSwitch", false)) {
            data.put("LAT", "34.0522342")
            data.put("LONG", "-118.2436849")
        } else {
            data.put("LAT", "")
            data.put("LONG", "")
        }
        val apiCall: Call<List<Event>> =
            RetrofitClient.getInstance().apIs.getEventsTable(data)
        Log.e("apicall", apiCall.toString())
        apiCall.enqueue(object : Callback<List<Event>?> {
            override fun onResponse(
                call: Call<List<Event>?>,
                response: Response<List<Event>?>
            ) {
                showProgress(false)
                events!!.addAll((response.body())!!)
                Log.e("events", Arrays.toString(events!!.toTypedArray()))
                recyclerView!!.setVisibility(View.VISIBLE)
                noEvents!!.setVisibility(View.GONE)
                val favIds: Set<String?> = favoriteIds
                for (i in events!!.indices) {
                    events!!.get(i).isFavorite = favIds.contains(events!!.get(i).id)
                }
                recyclerView!!.setLayoutManager(LinearLayoutManager(this@EventActivity))
                resultsAdapter = ResultsAdapter(events, this@EventActivity, this@EventActivity)
                recyclerView!!.setAdapter(resultsAdapter)
                resultsAdapter!!.notifyDataSetChanged()
            }

            public override fun onFailure(call: Call<List<Event>?>, t: Throwable) {
                Toast.makeText(this@EventActivity, "Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setAdapter(events: List<Event>) {}
    private fun showProgress(show: Boolean) {
        if (eventSpinner == null) eventSpinner = EventSpinner()
        if (show) eventSpinner!!.show(
            getSupportFragmentManager(),
            EventActivity::class.java.getName()
        ) else eventSpinner!!.dismissAllowingStateLoss()
    }

    public override fun likeClick(position: Int) {
        events!!.get(position).isFavorite = true
        addToFavorites(events!!.get(position))
        resultsAdapter!!.notifyDataSetChanged()
    }

    public override fun dislikeClick(position: Int) {
        events!!.get(position).isFavorite = false
        removeFromFavorites(events!!.get(position))
        resultsAdapter!!.notifyDataSetChanged()
    }

    fun addToFavorites(event: Event) {
        val events: MutableList<Event> = favorites
        val prefsEditor: SharedPreferences.Editor = mPrefs!!.edit()
        events.add(event)
        val json: String = gson!!.toJson(events)
        prefsEditor.putString("Favorites", json)
        prefsEditor.apply()
        showToast(event.name + " added to favorites")
    }

    fun removeFromFavorites(event: Event) {
        val events: MutableList<Event> = favorites.toMutableList()
        val prefsEditor: SharedPreferences.Editor = mPrefs!!.edit()
        for (i in events.indices) {
            if ((events.get(i).id == event.id)) {
                events.removeAt(i)
                break
            }
        }
        val json: String = gson!!.toJson(events)
        prefsEditor.putString("Favorites", json)
        prefsEditor.apply()
        showToast(event.name + " removed from favorites")
    }

    val favorites: MutableList<Event>
        get() {
            val empty: List<Event> = ArrayList()
            val json: String? = mPrefs!!.getString("Favorites", gson!!.toJson(empty))
            return gson!!.fromJson(json, object : TypeToken<List<Event?>?>() {}.getType())
        }
    val favoriteIds: Set<String?>
        get() {
            val favs: List<Event> = favorites
            val ids: MutableList<String?> = ArrayList()
            for (e: Event in favs) {
                ids.add(e.id)
            }
            return HashSet(ids)
        }

    private fun showToast(msg: String) {
        var msg: String? = msg
        if (msg == null) {
            msg = "Something went wrong!!"
        }
        Toast.makeText(this@EventActivity, msg, Toast.LENGTH_SHORT).show()
    }
}
package com.example.eventfinder.Fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventfinder.Adapter.ResultsAdapter
import com.example.eventfinder.Adapter.ResultsAdapter.ItemClicks
import com.example.eventfinder.Model.Event
import com.example.eventfinder.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FavouriteFragment constructor() : Fragment(), ItemClicks {
    private var events: MutableList<Event>? = null
    var gson: Gson? = null
    var mPrefs: SharedPreferences? = null
    var resultsAdapter: ResultsAdapter? = null
    var favRecyclerView: RecyclerView? = null
    var no_favorites: ImageView? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    public override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_favourite, container, false)
        no_favorites = view.findViewById(R.id.no_favorites)
        favRecyclerView = view.findViewById(R.id.favRecyclerView)
        gson = Gson()
        events = ArrayList()
        mPrefs = activity?.getSharedPreferences("pref", Context.MODE_PRIVATE)
        events?.addAll(favorites)
        favRecyclerView?.layoutManager = LinearLayoutManager(activity)
        resultsAdapter = ResultsAdapter(events, activity, this)
        favRecyclerView?.adapter = resultsAdapter
        if (events?.isEmpty() == true) {
            no_favorites?.visibility = View.VISIBLE
            favRecyclerView?.visibility = View.GONE
        } else {
            no_favorites?.visibility = View.GONE
            favRecyclerView?.visibility = View.VISIBLE
        }
        return view
    }

    public override fun likeClick(position: Int) {}
    public override fun dislikeClick(position: Int) {
        removeFromFavorites(events!!.get(position))
        events!!.removeAt(position)
        resultsAdapter!!.notifyItemRemoved(position)
        resultsAdapter!!.notifyDataSetChanged()
        if (events!!.isEmpty()) {
            no_favorites!!.setVisibility(View.VISIBLE)
            favRecyclerView!!.setVisibility(View.GONE)
        }
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
            if ((events[i].id == event.id)) {
                events.removeAt(i)
                break
            }
        }
        val json: String? = gson?.toJson(events)
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
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun newInstance(param1: String?, param2: String?): FavouriteFragment {
            val fragment: FavouriteFragment = FavouriteFragment()
            return fragment
        }
    }
}
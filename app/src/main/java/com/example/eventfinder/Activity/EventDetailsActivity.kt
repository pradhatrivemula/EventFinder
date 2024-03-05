package com.example.eventfinder.Activity

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ResolveInfo
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.eventfinder.Adapter.EventPageAdapter
import com.example.eventfinder.Adapter.ResultsAdapter.ItemClicks
import com.example.eventfinder.Client.RetrofitClient
import com.example.eventfinder.Fragment.ArtistFragment
import com.example.eventfinder.Fragment.EventDetailsFragment
import com.example.eventfinder.Fragment.VenueFragment
import com.example.eventfinder.Model.Event
import com.example.eventfinder.Model.EventDetailsModel
import com.example.eventfinder.R
import com.example.eventfinder.Util.EventSpinner
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class EventDetailsActivity constructor() : AppCompatActivity() {
    private var favorites: List<Event?>? = null
    private lateinit var events: Event
    var gson: Gson? = null
    var mPrefs: SharedPreferences? = null
    private var eventName: TextView? = null
    private var id: String? = null
    private var eventSpinner: EventSpinner? = null
    private var tabLayout: TabLayout? = null
    private var viewPager2: ViewPager2? = null
    private var back: ImageButton? = null
    private var fb: ImageButton? = null
    private var twitter: ImageButton? = null
    private var favv: ImageButton? = null
    private val tabIcons: IntArray = intArrayOf(
        R.drawable.info_icon,
        R.drawable.artist_icon,
        R.drawable.venue_icon
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)
        mPrefs = getSharedPreferences("pref", MODE_PRIVATE)
        gson = Gson()
        events = gson!!.fromJson(getIntent().getStringExtra("event"), Event::class.java)
        favorites = getFavorites()
        getEvents()
        eventName = findViewById(R.id.eventName)
        tabLayout = findViewById(R.id.event_tab_layout)
        viewPager2 = findViewById(R.id.event_view_pager)
        back = findViewById(R.id.backToSearch)
        fb = findViewById(R.id.fb)
        twitter = findViewById(R.id.twitter)
        favv = findViewById(R.id.eventFav)
        back?.setOnClickListener { onBackPressed() }
        eventName?.setSelected(true)
        back?.setBackgroundResource(0)
        fb?.setBackgroundResource(0)
        twitter?.setBackgroundResource(0)
        favv?.setBackgroundResource(0)
        fb?.setOnClickListener {
            val preferences: SharedPreferences = getSharedPreferences("pref", MODE_PRIVATE)
            val url: String? = preferences.getString("buyTicketsAt", "")
            var intent: Intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, url)
            var facebookAppFound: Boolean = false
            val matches: List<ResolveInfo> =
                packageManager.queryIntentActivities(intent, 0)
            for (info: ResolveInfo in matches) {
                if (info.activityInfo.packageName.lowercase(Locale.getDefault())
                        .startsWith("com.facebook.katana")
                ) {
                    intent.setPackage(info.activityInfo.packageName)
                    facebookAppFound = true
                    break
                }
            }
            if (!facebookAppFound) {
                val sharerUrl: String = "https://www.facebook.com/sharer/sharer.php?u=" + url
                intent = Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl))
            }
            startActivity(intent)
        }
        twitter?.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                val preferences: SharedPreferences = getSharedPreferences("pref", MODE_PRIVATE)
                val name: String? = preferences.getString("name", "")
                val url: String = "Check out" + name + "on TicketMaster!" + preferences.getString(
                    "buyTicketsAt",
                    ""
                )
                var intent: Intent = Intent(Intent.ACTION_SEND)
                intent.setType("text/plain")
                intent.putExtra(Intent.EXTRA_TEXT, url)
                var twitterAppFound: Boolean = false
                val matches: List<ResolveInfo> =
                    getPackageManager().queryIntentActivities(intent, 0)
                for (info: ResolveInfo in matches) {
                    if (info.activityInfo.packageName.lowercase(Locale.getDefault())
                            .startsWith("com.twitter.android")
                    ) {
                        intent.setPackage(info.activityInfo.packageName)
                        twitterAppFound = true
                        break
                    }
                }
                if (!twitterAppFound) {
                    val sharerUrl: String = "https://twitter.com/intent/tweet?text=" + url
                    intent = Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl))
                }
                startActivity(intent)
            }
        })
        val eventPageAdapter: EventPageAdapter =
            EventPageAdapter(supportFragmentManager, lifecycle)
        eventPageAdapter.addFragment(EventDetailsFragment())
        eventPageAdapter.addFragment(ArtistFragment())
        eventPageAdapter.addFragment(VenueFragment())
        viewPager2?.adapter = eventPageAdapter
        tabLayout?.let {
            viewPager2?.let { it1 ->
                TabLayoutMediator(
                    it, it1,
                    TabConfigurationStrategy { tab: TabLayout.Tab, position: Int ->
                        when (position) {
                            0 -> tab.text = "DETAILS"
                            1 -> tab.text = "ARTIST(S)"
                            2 -> tab.text = "VENUE"
                        }
                    }
                ).attach()
            }
        }
        tabLayout?.addOnTabSelectedListener(object : OnTabSelectedListener {
            public override fun onTabSelected(tab: TabLayout.Tab) {
                // Handle tab selection
                val tabIconColor: Int =
                    ContextCompat.getColor(this@EventDetailsActivity, R.color.toolbar_text)
                tab.getIcon()!!.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
                tabLayout?.invalidate()
            }

            public override fun onTabUnselected(tab: TabLayout.Tab) {
                // Handle tab unselection
                val tabIconColor: Int =
                    ContextCompat.getColor(this@EventDetailsActivity, R.color.white)
                tab.getIcon()!!.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
                tabLayout?.invalidate()
            }

            public override fun onTabReselected(tab: TabLayout.Tab) {
                // Handle tab reselection
            }
        })
        tabLayout?.getTabAt(0)!!.setIcon(tabIcons.get(0))
        tabLayout?.getTabAt(1)!!.setIcon(tabIcons.get(1))
        tabLayout?.getTabAt(2)!!.setIcon(tabIcons.get(2))

        if (events.isFavorite) {
            favv?.setImageDrawable(favv?.getContext()?.getDrawable(R.drawable.favorite_24))
        } else {
            favv?.setImageDrawable(favv?.getContext()?.getDrawable(R.drawable.favorite_border_24))
        }

        favv?.setOnClickListener {
            if (!events.isFavorite) {
                likeClick()
            } else {
                dislikeClick()
            }
        }
    }

    public override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun getEvents() {
        showProgress(true)
        val data: MutableMap<String, String?> = HashMap()
        val i: Intent = getIntent()
        id = i.getStringExtra("id")
        data.put("id", id)
        val apiCall: Call<EventDetailsModel> =
            RetrofitClient.Companion.getInstance().apIs.getEventsDetailsTable(data)
        Log.e("apicall", apiCall.toString())
        apiCall.enqueue(object : Callback<EventDetailsModel?> {
            public override fun onResponse(
                call: Call<EventDetailsModel?>,
                response: Response<EventDetailsModel?>
            ) {
                val events: EventDetailsModel? = response.body()
                setAdapter(events)
            }

            public override fun onFailure(call: Call<EventDetailsModel?>, t: Throwable) {
                Toast.makeText(this@EventDetailsActivity, "Error", Toast.LENGTH_SHORT).show()
                Log.e("msg", (t.message)!!)
            }
        })
    }

    private fun setAdapter(events: EventDetailsModel?) {
        val preferences: SharedPreferences = getSharedPreferences("pref", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preferences.edit()
        eventName?.setText(events?.name)
        editor.putString("id", id)
        editor.putString("name", events?.name)
        editor.putString("artist", events?.artist)
        editor.putString("venue", events?.venue)
        editor.putString("date", events?.date)
        editor.putString("time", events?.time)
        editor.putString("genre", events?.genre)
        editor.putString(
            "priceRange",
            events?.minPrice.toString() + " - " + events?.maxPrice + " (USD)"
        )
        editor.putString("ticketStatus", events?.status)
        editor.putString("buyTicketsAt", events?.url)
        editor.putString("eventImage", events?.seatMap)
        showProgress(false)

        //venue data
        editor.putString("venueName", events?.venueData?.name)
        editor.putString("venueAdd", events?.venueData?.address)
        editor.putString(
            "venueCity",
            events?.venueData?.city + "," + events?.venueData?.state
        )
        editor.putString("venueContact", events?.venueData?.phoneNo)
        editor.putString("childRules", events?.venueData?.childRules)
        editor.putString("generalRules", events?.venueData?.generalRules)
        editor.putString("openHours", events?.venueData?.openHours)
        editor.putString("markerLat", events?.venueData?.center?.lat.toString())
        editor.putString("markerLong", events?.venueData?.center?.lng.toString())
        editor.apply()
    }

    private fun showProgress(show: Boolean) {
        if (eventSpinner == null) eventSpinner = EventSpinner()
        if (show) eventSpinner!!.show(
            getSupportFragmentManager(),
            EventDetailsActivity::class.java.getName()
        ) else eventSpinner!!.dismissAllowingStateLoss()
    }

    fun likeClick() {
        events?.isFavorite = true
        addToFavorites(events)
    }

    fun dislikeClick() {
        events?.isFavorite = false
        removeFromFavorites(events)
    }

//    public override fun likeClick(position: Int) {
//        events!!.isFavorite = true
//        addToFavorites(events)
//        //resultsAdapter.notifyDataSetChanged();
//    }
//
//    public override fun dislikeClick(position: Int) {
//        events!!.isFavorite = false
//        removeFromFavorites(events)
//        //resultsAdapter.notifyDataSetChanged();
//    }

    fun addToFavorites(event: Event?) {
        val events: MutableList<Event?> = getFavorites()
        val prefsEditor: SharedPreferences.Editor = mPrefs!!.edit()
        events.add(event)
        val json: String = gson!!.toJson(events)
        prefsEditor.putString("Favorites", json)
        prefsEditor.apply()
        showToast(event?.name + " added to favorites")
    }

    fun removeFromFavorites(event: Event?) {
        val events: MutableList<Event?> = getFavorites().toMutableList()
        val prefsEditor: SharedPreferences.Editor = mPrefs!!.edit()
        for (i in events.indices) {
            if ((events.get(i)?.id == event?.id)) {
                events.removeAt(i)
                break
            }
        }
        val json: String = gson!!.toJson(events)
        prefsEditor.putString("Favorites", json)
        prefsEditor.apply()
        showToast(event?.name + " removed from favorites")
    }

    fun getFavorites(): MutableList<Event?> {
        val empty: List<Event> = ArrayList()
        val json: String? = mPrefs!!.getString("Favorites", gson!!.toJson(empty))
        return gson!!.fromJson(json, object : TypeToken<List<Event?>?>() {}.getType())
    }

    val favoriteIds: Set<String?>
        get() {
            val favs: List<Event?> = getFavorites()
            val ids: MutableList<String?> = ArrayList()
            for (e: Event? in favs) {
                ids.add(e?.id)
            }
            return HashSet(ids)
        }

    private fun showToast(msg: String) {
        var msg: String? = msg
        if (msg == null) {
            msg = "Something went wrong!!"
        }
        Toast.makeText(this@EventDetailsActivity, msg, Toast.LENGTH_SHORT).show()
    }
}
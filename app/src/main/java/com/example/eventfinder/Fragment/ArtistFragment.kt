package com.example.eventfinder.Fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventfinder.Activity.EventDetailsActivity
import com.example.eventfinder.Adapter.ArtistAdapter
import com.example.eventfinder.Client.RetrofitClient
import com.example.eventfinder.Model.EventDetailsModel
import com.example.eventfinder.R
import com.example.eventfinder.Util.EventSpinner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArtistFragment constructor() : Fragment() {
    private var eventSpinner: EventSpinner? = null
    private var artistRecyclerView: RecyclerView? = null
    private var no_artists: TextView? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    public override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        events

        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_artist, container, false)
        artistRecyclerView = view.findViewById(R.id.artistRecyclerView)
        no_artists = view.findViewById(R.id.no_artists)
        return view
    }

    private val events: Unit
        private get() {
            showProgress(true)
            val preferences: SharedPreferences? = activity?.getSharedPreferences("pref", Context.MODE_PRIVATE)
            val data: MutableMap<String, String?> = HashMap()
            val id: String? = preferences?.getString("id", "")
            data.put("id", id)
            val apiCall: Call<EventDetailsModel>? =
                RetrofitClient.Companion.getInstance()?.apIs?.getEventsDetailsTable(data)
            Log.e("apicall", apiCall.toString())
            apiCall?.enqueue(object : Callback<EventDetailsModel?> {
                public override fun onResponse(
                    call: Call<EventDetailsModel?>,
                    response: Response<EventDetailsModel?>
                ) {
                    showProgress(false)
                    val events: EventDetailsModel? = response.body()
                    if (events != null) {
                        artistRecyclerView!!.setVisibility(View.VISIBLE)
                        no_artists!!.setVisibility(View.GONE)
                        artistRecyclerView!!.setLayoutManager(LinearLayoutManager(getActivity()))
                        val resultsAdapter: ArtistAdapter = ArtistAdapter(events, getActivity())
                        artistRecyclerView!!.setAdapter(resultsAdapter)
                    } else {
                        artistRecyclerView!!.setVisibility(View.GONE)
                        no_artists!!.setVisibility(View.VISIBLE)
                    }
                }

                public override fun onFailure(call: Call<EventDetailsModel?>, t: Throwable) {
                    Log.e("msg", (t.message)!!)
                }
            })
        }

    private fun showProgress(show: Boolean) {
        if (eventSpinner == null) eventSpinner = EventSpinner()
        if (show) eventSpinner!!.show(
            getChildFragmentManager(),
            EventDetailsActivity::class.java.getName()
        ) else eventSpinner!!.dismissAllowingStateLoss()
    }
}
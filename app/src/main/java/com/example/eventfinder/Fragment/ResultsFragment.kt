package com.example.eventfinder.Fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventfinder.Adapter.ResultsAdapter
import com.example.eventfinder.Adapter.ResultsAdapter.ItemClicks
import com.example.eventfinder.Client.RetrofitClient
import com.example.eventfinder.Listener.OnFragmentInteractionListener
import com.example.eventfinder.MainActivity
import com.example.eventfinder.Model.Event
import com.example.eventfinder.R
import com.example.eventfinder.Util.EventSpinner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResultsFragment constructor() : Fragment(), ItemClicks {
    private var events: List<Event>? = null
    var resultsAdapter: ResultsAdapter? = null
    private var eventSpinner: EventSpinner? = null
    private var mContext: Context? = null
    private var mListener: OnFragmentInteractionListener? = null
    private var recyclerView: RecyclerView? = null
    private var back: Button? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    public override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        try {
            mListener = context as OnFragmentInteractionListener?
        } catch (e: ClassCastException) {
            throw ClassCastException(
                (context.toString()
                        + " must implement OnFragmentInteractionListener")
            )
        }
    }

    public override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_results, container, false)
        recyclerView = view.findViewById(R.id.recyclerView1)
        recyclerView?.layoutManager = LinearLayoutManager(mContext)
        events = ArrayList()
        resultsAdapter = ResultsAdapter(events, mContext, this)
        recyclerView?.adapter = resultsAdapter
        back = view.findViewById(R.id.back1)
        back?.setOnClickListener { mListener!!.changeFragment(2) }

        //getEvents();
        return view
    }

    private fun getEvents() {
        showProgress(true)
        val preferences: SharedPreferences? = activity?.getSharedPreferences("pref", Context.MODE_PRIVATE)
        val data: MutableMap<String, String?> = HashMap()
        data.put("keyword", preferences?.getString("keyword", null))
        data.put("dist", preferences?.getString("distance", null))
        data.put("CAT", preferences?.getString("category", null))
        data.put("LAT", preferences?.getString("latitude", null))
        data.put("LONG", preferences?.getString("longitude", null))
        val apiCall: Call<List<Event>>? =
            RetrofitClient.Companion.getInstance()?.apIs?.getEventsTable(data)
        Log.e("apicall", apiCall.toString())
        apiCall?.enqueue(object : Callback<List<Event>?> {
            public override fun onResponse(
                call: Call<List<Event>?>,
                response: Response<List<Event>?>
            ) {
                showProgress(false)
                val event: List<Event>? = response.body()
                setAdapter(event)
            }

            public override fun onFailure(call: Call<List<Event>?>, t: Throwable) {
                Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setAdapter(event: List<Event>?) {
        resultsAdapter = ResultsAdapter(event, mContext, this)
        resultsAdapter?.notifyDataSetChanged()
    }

    fun showProgress(show: Boolean) {
        if (eventSpinner == null) eventSpinner = EventSpinner()
        if (show) eventSpinner!!.show(
            getChildFragmentManager(),
            MainActivity::class.java.getName()
        ) else eventSpinner!!.dismissAllowingStateLoss()
    }

    override fun likeClick(position: Int) {}
    override fun dislikeClick(position: Int) {}
}
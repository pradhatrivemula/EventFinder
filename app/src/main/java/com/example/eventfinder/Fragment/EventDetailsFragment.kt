package com.example.eventfinder.Fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.eventfinder.R

class EventDetailsFragment constructor() : Fragment() {
    private var artistTeam: TextView? = null
    private var venue: TextView? = null
    private var date: TextView? = null
    private var time: TextView? = null
    private var genre: TextView? = null
    private var priceRange: TextView? = null
    private var ticketStatus: TextView? = null
    private var buyTicketsAt: TextView? = null
    private var eventImage: ImageView? = null
    var preferences: SharedPreferences? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    public override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        preferences = activity?.getSharedPreferences("pref", Context.MODE_PRIVATE)
        val view: View = inflater.inflate(R.layout.fragment_event_details, container, false)
        artistTeam = view.findViewById(R.id.artistTeam)
        venue = view.findViewById(R.id.venue)
        date = view.findViewById(R.id.date)
        time = view.findViewById(R.id.time)
        genre = view.findViewById(R.id.genre)
        priceRange = view.findViewById(R.id.priceRange)
        ticketStatus = view.findViewById(R.id.ticketStatus)
        buyTicketsAt = view.findViewById(R.id.buyTicketsAt)
        eventImage = view.findViewById(R.id.eventImage)
        artistTeam?.isSelected = true
        genre?.isSelected = true
        buyTicketsAt?.isSelected = true
        buyTicketsAt?.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                val browserIntent: Intent = Intent(Intent.ACTION_VIEW)
                browserIntent.setData(Uri.parse(preferences?.getString("buyTicketsAt", "")))
                startActivity(browserIntent)
            }
        })
        artistTeam?.text = preferences?.getString("artist", "")
        venue?.text = preferences?.getString("venue", "")
        date?.text = preferences?.getString("date", "")
        time?.text = preferences?.getString("time", "")
        genre?.text = preferences?.getString("genre", "")
        priceRange?.text = preferences?.getString("priceRange", "")
        ticketStatus?.text = preferences?.getString("ticketStatus", "")
        val content: SpannableString = SpannableString(preferences?.getString("buyTicketsAt", ""))
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        buyTicketsAt?.setText(content)
        eventImage?.let {
            Glide.with(this).load(preferences?.getString("eventImage", ""))
                .placeholder(R.drawable.baseline_person_24).error(R.drawable.baseline_person_24)
                .into(it)
        }
        checkStatus()
        return view
    }

    private fun checkStatus() {
        if ((ticketStatus!!.getText().toString() == "offsale")) {
            ticketStatus!!.setBackgroundResource(R.drawable.rounded_corner_off)
        } else {
            ticketStatus!!.setBackgroundResource(R.drawable.rounded_corner)
        }
    }
}
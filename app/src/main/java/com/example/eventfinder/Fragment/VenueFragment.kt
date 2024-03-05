package com.example.eventfinder.Fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import com.example.eventfinder.R
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class VenueFragment constructor() : Fragment(), OnMapReadyCallback {
    private var venueName: TextView? = null
    private var venueAdd: TextView? = null
    private var venueCity: TextView? = null
    private var venueContact: TextView? = null
    private var openHours: TextView? = null
    private var generalRules: TextView? = null
    private var childRules: TextView? = null
    private var preferences: SharedPreferences? = null
    private var gMap: GoogleMap? = null
    var markerLat: String? = null
    var markerLong: String? = null
    private var nestedView: NestedScrollView? = null
    var marker: Marker? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    public override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        preferences = activity?.getSharedPreferences("pref", Context.MODE_PRIVATE)
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_venue, container, false)
        venueName = view.findViewById(R.id.venueName)
        venueAdd = view.findViewById(R.id.venueAddress)
        venueCity = view.findViewById(R.id.venueCity)
        venueContact = view.findViewById(R.id.venueContact)
        openHours = view.findViewById(R.id.openHours)
        generalRules = view.findViewById(R.id.generalRules)
        childRules = view.findViewById(R.id.childRules)
        nestedView = view.findViewById(R.id.nestedView)
        venueAdd?.isSelected = true
        venueCity?.isSelected = true
        venueContact?.isSelected = true
        venueName?.text = preferences?.getString("venueName", "")
        venueAdd?.text = preferences?.getString("venueAdd", "")
        venueCity?.text = preferences?.getString("venueCity", "")
        venueContact?.text = preferences?.getString("venueContact", "")
        openHours?.text = preferences?.getString("openHours", "")
        generalRules?.text = preferences?.getString("generalRules", "")
        childRules?.text = preferences?.getString("childRules", "")
        generalRules?.setOnClickListener {
            if (generalRules?.maxLines != Int.MAX_VALUE) generalRules?.maxLines =
                Int.MAX_VALUE else generalRules?.maxLines =
                3
        }
        childRules?.setOnClickListener {
            if (childRules?.maxLines != Int.MAX_VALUE) childRules?.maxLines =
                Int.MAX_VALUE else childRules?.maxLines = 3
        }
        val mapFragment: SupportMapFragment? = childFragmentManager
            .findFragmentById(R.id.googleMaps) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        return view
    }

    public override fun onMapReady(googleMap: GoogleMap) {
        markerLat = preferences!!.getString("markerLat", "")
        markerLong = preferences!!.getString("markerLong", "")
        gMap = googleMap
        gMap!!.getUiSettings().setZoomControlsEnabled(true)
        val loc: LatLng = LatLng(markerLat!!.toDouble(), markerLong!!.toDouble())
        gMap!!.addMarker(MarkerOptions().position(loc).title("Event Location"))
        val location: CameraUpdate = CameraUpdateFactory.newLatLngZoom(
            loc, 15f
        )
        gMap!!.animateCamera(location)
        //        gMap.setOnMapClickListener(latLng -> {
//            String geoUri = "http://maps.google.com/maps?q=loc:" + markerLat + "," + markerLong;
//            Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
//            startActivity(mapIntent);
//        });
//        gMap.setOnMarkerClickListener(marker -> {
//            String geoUri = "http://maps.google.com/maps?q=loc:" + markerLat + "," + markerLong;
//            Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
//            startActivity(mapIntent);
//            return false;
//        });
    }
}
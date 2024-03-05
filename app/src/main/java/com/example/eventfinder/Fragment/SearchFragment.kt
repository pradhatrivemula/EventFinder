package com.example.eventfinder.Fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.eventfinder.Activity.EventActivity
import com.example.eventfinder.Adapter.KeywordSearchAdapter
import com.example.eventfinder.Listener.OnFragmentInteractionListener
import com.example.eventfinder.R
import com.google.android.material.snackbar.Snackbar

class SearchFragment constructor() : Fragment(), AdapterView.OnItemSelectedListener {
    private var mListener: OnFragmentInteractionListener? = null
    private val category: Array<String?> =
        arrayOf("All", "Music", "Sports", "Arts & Theatre", "Film", "Miscellaneous")
    private var distance: EditText? = null
    private var location: EditText? = null
    private var keyword: AutoCompleteTextView? = null
    private var categorySpinner: Spinner? = null
    private var locationSwitch: SwitchCompat? = null
    private var mContext: Context? = null
    private var search: Button? = null
    private var clear: Button? = null
    private var selectedValue: String? = null
    var locationManager: LocationManager? = null
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var preferences: SharedPreferences? = null
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

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    public override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val currentView: View = inflater.inflate(R.layout.fragment_search, container, false)
        preferences = activity?.getSharedPreferences("pref", Context.MODE_PRIVATE)
        val locationListener: LocationListener = object : LocationListener {
            public override fun onLocationChanged(location: Location) {
                // This method will be called when the location changes
                latitude = location.getLatitude()
                longitude = location.getLongitude()
                // Do something with the latitude and longitude
            }
        }
        locationManager = mContext!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        if (ContextCompat.checkSelfPermission(
                (mContext)!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                (mContext)!!, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is already granted
            // Perform the operation that requires this permission
        } else {
            // Permission is not granted yet, ask for permission
            ActivityCompat.requestPermissions(
                (getActivity())!!,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                1001
            )
        }
        locationManager?.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0,
            0f,
            locationListener
        )
        Log.e("Location", "$latitude,$longitude")
        keyword = currentView.findViewById(R.id.keyword)
        distance = currentView.findViewById(R.id.distance)
        location = currentView.findViewById(R.id.location)
        search = currentView.findViewById(R.id.search)
        search?.setBackgroundColor(Color.argb(255, 80, 195, 27))
        clear = currentView.findViewById(R.id.clear)
        clear?.setBackgroundColor(Color.argb(255, 236, 116, 46))
        categorySpinner = currentView.findViewById(R.id.spinner)
        categorySpinner?.onItemSelectedListener = this
        val ad: ArrayAdapter<*> = ArrayAdapter<Any?>(
            (mContext)!!,
            android.R.layout.simple_spinner_item,
            category
        )
        ad.setDropDownViewResource(R.layout.spinner)
        categorySpinner?.adapter = ad
        locationSwitch = currentView.findViewById(R.id.categorySwitch)
        locationSwitch?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                location?.setText(latitude.toString() + "" + longitude)
                locationSwitch?.thumbDrawable?.setColorFilter(
                    resources.getColor(R.color.toolbar_text),
                    PorterDuff.Mode.MULTIPLY
                )
                locationSwitch?.trackDrawable?.setColorFilter(
                    resources.getColor(R.color.track),
                    PorterDuff.Mode.MULTIPLY
                )
                location?.visibility = View.GONE
            } else {
                locationSwitch?.thumbDrawable?.setColorFilter(
                    resources.getColor(R.color.switch_thumb),
                    PorterDuff.Mode.MULTIPLY
                )
                locationSwitch?.trackDrawable?.setColorFilter(
                    resources.getColor(R.color.switch_track),
                    PorterDuff.Mode.MULTIPLY
                )
                location?.visibility = View.VISIBLE
            }
        }
        keyword?.setAdapter(KeywordSearchAdapter((mContext)!!, android.R.layout.simple_list_item_1))
        keyword?.onItemClickListener =
            OnItemClickListener { parent, view, position, id -> selectedValue = parent.getItemAtPosition(position) as String? }
        search?.setOnClickListener {
            if (keyword?.getText().toString().isEmpty() || distance?.getText().toString()
                    .isEmpty() || location?.getText().toString().isEmpty()
            ) {
                val imm: InputMethodManager =
                    mContext!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentView.getWindowToken(), 0)
                val snackbar: Snackbar =
                    Snackbar.make(currentView, "Please fill all fields", Snackbar.LENGTH_SHORT)
                snackbar.show()
            } else {

                //mListener.changeFragment(1);
                val intent: Intent = Intent(getActivity(), EventActivity::class.java)
                startActivity(intent)
                val editor: SharedPreferences.Editor? = preferences?.edit()
                editor?.putString("keyword", keyword?.getText().toString())
                editor?.putString("distance", distance?.getText().toString())
                locationSwitch?.isChecked?.let { it1 -> editor?.putBoolean("locationSwitch", it1) }
                if (locationSwitch?.isChecked == true) {
                    editor?.putString("latitude", latitude.toString())
                    editor?.putString("longitude", longitude.toString())
                }
                editor?.putString("location", location?.text.toString())
                editor?.apply()
            }
        }
        clear?.setOnClickListener {
            keyword?.setText("")
            distance?.setText("10")
            categorySpinner?.setSelection(0)
            location?.setText("")
            locationSwitch?.isChecked = false
        }
        return currentView
    }

    public override fun onItemSelected(
        parent: AdapterView<*>,
        view: View,
        position: Int,
        id: Long
    ) {
        var selectedItem: String? = parent.getItemAtPosition(position).toString()
        val selectedText: TextView = parent.getChildAt(0) as TextView
        selectedText.setTextColor(Color.WHITE)
        val editor: SharedPreferences.Editor = preferences!!.edit()
        when (selectedItem) {
            "All" -> selectedItem = "Default"
            "Music" -> selectedItem = "Music"
            "Sports" -> selectedItem = "Sports"
            "Arts & Theatre" -> selectedItem = "ArtsNTheatre"
            "Film" -> selectedItem = "Film"
            "Miscellaneous" -> selectedItem = ""
            else -> {}
        }
        editor.putString("category", selectedItem)
        editor.apply()
    }

    public override fun onNothingSelected(parent: AdapterView<*>?) {}
    public override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}
package com.example.eventfinder.Model

import java.io.Serializable

class EventDetailsModel : Serializable {
    val id: String? = null
    val name: String? = null
    val url: String? = null
    val date: String? = null
    val artist: String? = null
    val time: String? = null
    val venue: String? = null
    val genre: String? = null
    val status: String? = null
    val minPrice = 0
    val maxPrice = 0
    val currency: String? = null
    val seatMap: String? = null
    val venueData: VenueData? = null
    var spotify: List<Spotify?>? = null

    inner class VenueData {
        val name: String? = null
        val address: String? = null
        val city: String? = null
        val state: String? = null
        val phoneNo: String? = null
        val openHours: String? = null
        val generalRules: String? = null
        val childRules: String? = null
        val center: Center? = null
    }

    inner class Center {
        val lat = 0.0
        val lng = 0.0
    }

    inner class Spotify {
        var artist: Artist? = null
        var album: List<Album?>? = null
    }

    inner class Album {
        var iD: String? = null
        var img: String? = null
    }

    inner class Artist {
        var uRL: String? = null
        var iD: String? = null
        var img: String? = null
        var name: String? = null
        var popularity: Long = 0
        var followers: String? = null
    }
}
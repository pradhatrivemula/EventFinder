package com.example.eventfinder.Adapter

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventfinder.Adapter.ArtistAdapter.ArtistHolderRetrofit
import com.example.eventfinder.Model.EventDetailsModel
import com.example.eventfinder.R
import com.google.android.material.progressindicator.CircularProgressIndicator

class ArtistAdapter(var resultsModels: EventDetailsModel, var mContext: Context?) :
    RecyclerView.Adapter<ArtistHolderRetrofit>() {
    private val mImageIds: List<String> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistHolderRetrofit {
        val view = LayoutInflater.from(mContext).inflate(R.layout.artist_list_layout, parent, false)
        mContext = parent.context
        return ArtistHolderRetrofit(view)
    }

    override fun onBindViewHolder(holder: ArtistHolderRetrofit, position: Int) {
        val pos = position
        holder.artistName.text = resultsModels.spotify?.get(position)?.artist?.name
        val follower =
            resultsModels.spotify?.get(position)?.artist?.followers?.replace(",", "")?.toInt()
                ?.div(1000000)
        holder.artistFollower.text = follower.toString()
//        holder.artistSpotify.paintFlags =
//            holder.artistSpotify.paintFlags or Paint.UNDERLINE_TEXT_FLAG
//        holder.artistSpotify.setOnClickListener(View.OnClickListener {
//            openUrl(
//                resultsModels.spotify?.get(pos)?.artist?.uRL
//            )
//        })
        holder.percentage.text = resultsModels.spotify?.get(position)?.artist?.popularity.toString()
        holder.percentageCircle.progress =
            resultsModels.spotify?.get(position)?.artist?.popularity?.toInt()!!
        Glide.with((mContext)!!).load(resultsModels.spotify!![position]?.album?.get(0)?.img)
            .placeholder(R.drawable.baseline_person_24).error(R.drawable.baseline_person_24)
            .into(holder.popAl1)
//        Glide.with((mContext)!!).load(resultsModels.spotify!![position]?.album?.get(1)?.img)
//            .placeholder(R.drawable.baseline_person_24).error(R.drawable.baseline_person_24)
//            .into(holder.popAl2)
//        Glide.with((mContext)!!).load(resultsModels.spotify!![position]?.album?.get(2)?.img)
//            .placeholder(R.drawable.baseline_person_24).error(R.drawable.baseline_person_24)
//            .into(holder.popAl3)
        Glide.with((mContext)!!).load(resultsModels.spotify!![position]?.artist?.img)
            .placeholder(R.drawable.baseline_person_24).error(R.drawable.baseline_person_24)
            .into(holder.imageView)

//        for (int i = 0; i <= resultsModels.getSpotify().get(position).getAlbum().size(); i++) {
//            mImageIds.add(resultsModels.getSpotify().get(i).getAlbum().get(i).getImg());
//        }
    }

    private fun openUrl(url: String?) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        mContext!!.startActivity(browserIntent)
    }

    override fun getItemCount(): Int {
        return resultsModels.spotify?.size ?: 3
    }

    inner class ArtistHolderRetrofit(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        var popAl1: ImageView
//        var popAl2: ImageView
//        var popAl3: ImageView
        var artistName: TextView
        var artistFollower: TextView
        //var artistSpotify: TextView
        var percentage: TextView
        var percentageCircle: CircularProgressIndicator

        init {
            popAl1 = itemView.findViewById(R.id.popAl1)
//            popAl2 = itemView.findViewById(R.id.popAl2)
//            popAl3 = itemView.findViewById(R.id.popAl3)
            imageView = itemView.findViewById(R.id.artistImageView)
            artistName = itemView.findViewById(R.id.artistName)
            artistFollower = itemView.findViewById(R.id.artistFollower)
            //artistSpotify = itemView.findViewById(R.id.artistSpotify)
            percentage = itemView.findViewById(R.id.percentage)
            percentageCircle = itemView.findViewById(R.id.percentageCircle)
        }
    }
}
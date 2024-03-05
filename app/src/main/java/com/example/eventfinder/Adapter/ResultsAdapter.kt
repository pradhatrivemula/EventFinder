package com.example.eventfinder.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventfinder.Activity.EventDetailsActivity
import com.example.eventfinder.Adapter.ResultsAdapter.ResultsHolderRetrofit
import com.example.eventfinder.Model.Event
import com.example.eventfinder.R
import com.google.gson.Gson

class ResultsAdapter constructor(
    var resultsModels: List<Event>?,
    var mContext: Context?,
    private val itemClicks: ItemClicks
) : RecyclerView.Adapter<ResultsHolderRetrofit>() {
    var gson: Gson? = null
    public override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ResultsHolderRetrofit {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.list_layout, parent, false)
        mContext = parent.getContext()
        gson = Gson()
        return ResultsHolderRetrofit(view)
    }

    public override fun onBindViewHolder(holder: ResultsHolderRetrofit, position: Int) {
        Log.e("name", resultsModels!![position].name)
        val event: Event = resultsModels!![position]
        val id: String = resultsModels!![position].id
        val pos: Int = position
        holder.name.text = event.name
        holder.date.text = event.localDate
        holder.category.text = event.genre
        holder.time.text = event.localTime
        holder.stadium.text = event.venue
        Glide.with((mContext)!!).load(event.imgUrl).placeholder(R.drawable.baseline_person_24)
            .error(R.drawable.baseline_person_24).into(holder.imageView)
        holder.cardView.setOnClickListener {
            val i: Intent = Intent(mContext, EventDetailsActivity::class.java)
            i.putExtra("id", id)
            i.putExtra("event", gson!!.toJson(event))
            mContext!!.startActivity(i)
        }
        if (event.isFavorite) {
            holder.fav.setImageDrawable(holder.fav.getContext().getDrawable(R.drawable.favorite_24))
        } else {
            holder.fav.setImageDrawable(
                holder.fav.getContext().getDrawable(R.drawable.favorite_border_24)
            )
        }
        holder.fav.setOnClickListener {
            if (!event.isFavorite) {
                itemClicks.likeClick(pos)
            } else {
                itemClicks.dislikeClick(pos)
            }
        }
    }

    public override fun getItemCount(): Int {
        return resultsModels!!.size
    }

    inner class ResultsHolderRetrofit constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        var cardView: CardView
        var fav: ImageButton
        var name: TextView
        var date: TextView
        var time: TextView
        var stadium: TextView
        var category: TextView

        init {
            cardView = itemView.findViewById(R.id.cardView)
            imageView = itemView.findViewById(R.id.imageView)
            fav = itemView.findViewById(R.id.fav)
            name = itemView.findViewById(R.id.name)
            date = itemView.findViewById(R.id.date)
            time = itemView.findViewById(R.id.time)
            stadium = itemView.findViewById(R.id.staduim)
            category = itemView.findViewById(R.id.category)
            name.setSelected(true)
            fav.setBackgroundResource(0)
        }
    }

    open interface ItemClicks {
        fun likeClick(position: Int)
        fun dislikeClick(position: Int)
    }
}
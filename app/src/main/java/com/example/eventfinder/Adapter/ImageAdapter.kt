package com.example.eventfinder.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventfinder.R

class ImageAdapter(private val mImageIds: List<String>, var mContext: Context) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView

        init {
            imageView = view.findViewById<View>(R.id.imageItemView) as ImageView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(mContext).load(mImageIds[position]).placeholder(R.drawable.baseline_person_24)
            .error(R.drawable.baseline_person_24).into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return mImageIds.size
    }
}
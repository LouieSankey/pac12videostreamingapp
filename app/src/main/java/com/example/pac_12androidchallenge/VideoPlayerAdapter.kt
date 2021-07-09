package com.example.pac_12androidchallenge

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.pac_12androidchallenge.model.VOD


//extends the adapter class player class
class VideoPlayerAdapter(private val vod: ArrayList<VOD>, private var requestManager: RequestManager) : RecyclerView.Adapter<VideoPlayerViewHolder>() {


    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoPlayerViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.vod_item, parent, false)
        return VideoPlayerViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: VideoPlayerViewHolder, position: Int) {
        holder.onBind(vod[position], requestManager)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return vod.size
    }

}
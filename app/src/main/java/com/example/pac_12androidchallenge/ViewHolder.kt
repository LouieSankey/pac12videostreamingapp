package com.example.pac_12androidchallenge

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.pac_12androidchallenge.model.VOD
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class VideoPlayerViewHolder(var parent: View) : RecyclerView.ViewHolder(parent) {

//    var media_container: FrameLayout = parent.findViewById(R.id.media_container)
    private var title: TextView = parent.findViewById(R.id.title)
    var thumbnail: ImageView = parent.findViewById(R.id.thumbnail)
    var progressBar: ProgressBar = parent.findViewById(R.id.progressBar)
    var requestManager: RequestManager? = null

    private val client = OkHttpClient()
    var schools: TextView = parent.findViewById(R.id.schools)
    var sports: TextView = parent.findViewById(R.id.sports)


    fun onBind(vod: VOD, requestManager: RequestManager?) {

        this.requestManager = requestManager
        parent.tag = this
        title.text = vod.title
        schools.text = ""
        sports.text = ""
        this.requestManager
            ?.load(vod.thumbnail)
            ?.into(thumbnail)

        val schoolsId = vod.schools.toString()
        val sportsId = vod.sports.toString()

        if(schoolsId !== "0"){
            apiSubRequest(schools, "schools/$schoolsId")
        }

        if(sportsId !== "0"){
            apiSubRequest(sports, "sports/$sportsId")
        }
    }

    fun apiSubRequest(textView: TextView, uri: String){

        val request = Request.Builder()
            .url("https://api.pac-12.com/v3/$uri")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                try {
                    response.body.use { responseBody ->
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")
                        val responseHeaders = response.headers
                        run {
                            var i = 0
                            val size = responseHeaders.size
                            while (i < size) {
                                println(responseHeaders.name(i) + ": " + responseHeaders.value(i))
                                i++
                            }
                        }
                        val jsonData = responseBody!!.string()
                        val Jobject = JSONObject(jsonData)

                        Handler(Looper.getMainLooper()).post(Runnable {
                            Log.d("school name", Jobject.getString("name"))
                            textView.text = Jobject.getString("name")
                        })

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        })
    }



}
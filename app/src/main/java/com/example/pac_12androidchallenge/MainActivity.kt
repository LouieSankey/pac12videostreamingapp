package com.example.pac_12androidchallenge

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.pac_12androidchallenge.databinding.ActivityMainBinding
import com.example.pac_12androidchallenge.model.VOD
import okhttp3.*

import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

    class MainActivity : AppCompatActivity() {

        private lateinit var binding: ActivityMainBinding
        private val client = OkHttpClient()
        private val vods: ArrayList<VOD> = ArrayList()

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            binding = ActivityMainBinding.inflate(layoutInflater)
            val view = binding.root
            setContentView(view)

            binding.recyclerView.adapter = VideoPlayerAdapter(vods, initGlide(this))
            binding.recyclerView.setVODs(vods)
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
            binding.recyclerView.hasFixedSize()

            getVOCPage()

    }

        fun getVOCPage() {
            val request = Request.Builder()
                .url("https://api.pac-12.com/v3/vod")
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
                            val Jarray = Jobject.getJSONArray("programs")


                            for (i in 0 until Jarray.length()) {
                                val `object` = Jarray.getJSONObject(i)

                                Log.d( "array", `object`.toString())

                                val obj = VOD(
                                    `object`.getString("title"),
                                    `object`.getString("manifest_url"),
                                    `object`.getJSONObject("images").getString("medium"),
                                    `object`.getInt("duration"),
                                    0,
                                    0,
                                )

                                if(`object`.has("schools") && !`object`.isNull("schools") ){
                                    obj.schools = `object`.getJSONArray("schools").getJSONObject(0).getInt("id")

                                }
                                if(`object`.has("sports") && !`object`.isNull("sports") ){
                                    obj.sports = `object`.getJSONArray("sports").getJSONObject(0).getInt("id")

                                }

                                vods.add(obj)
                            }

                            runOnUiThread { // Stuff that updates the UI
                                binding.recyclerView.setVODs(vods)
                                binding.recyclerView.adapter?.notifyDataSetChanged();
                            }
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            })
        }


    }

private fun initGlide(context : Context): RequestManager {
    val options = RequestOptions()
        .placeholder(R.drawable.white_background)
        .error(R.drawable.white_background)
    return Glide.with(context)
        .setDefaultRequestOptions(options)
}
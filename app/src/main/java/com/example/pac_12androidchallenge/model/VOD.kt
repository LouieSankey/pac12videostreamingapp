package com.example.pac_12androidchallenge.model

import org.json.JSONArray
import org.json.JSONObject

data class VOD(
    var title: String,
    var manifest_url: String,
    var thumbnail: String,
    var duration: Int,
    var schools: Int,
    var sports: Int,

    )

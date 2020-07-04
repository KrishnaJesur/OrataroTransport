package com.edusunsoft.transport.orataro.network

import com.edusunsoft.transport.orataro.ui.activitymaps.DirectionsResModel
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface GetDirectionInterface{

    @POST("/maps/api/directions/json?")
    fun getDirections(@Query("origin") origin: String,
                      @Query("destination") destination: String,
                      @Query("waypoints") waypoints: String,
                      @Query("sensor") sensor: Boolean,
                      @Query("mode") mode: String,
                      @Query("key") apiKey: String,
                      @Query("alternatives") alternatives: Boolean): Call<DirectionsResModel>
}
package com.example.weatherkotlinapp

import android.annotation.SuppressLint
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.second_fragment.*
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt



class SecondFragment : Fragment(){
    private lateinit var geocoder: Geocoder
    private lateinit var textViewTitle: TextView
    private lateinit var textViewDegrees: TextView
    private lateinit var textViewHumidity: TextView
    private lateinit var textViewWind: TextView
    private lateinit var textViewWeatherDescription: TextView
    private lateinit var textViewTimeOfDay: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.second_fragment, container, false)
        textViewDegrees = view.findViewById(R.id.textViewDegrees)
        textViewTitle=view.findViewById(R.id.titleOfSecondFragment)
        textViewWeatherDescription=view.findViewById(R.id.textViewWeatherDescription)
        textViewHumidity=view.findViewById(R.id.textViewHumidity)
        textViewHumidity.setCompoundDrawablesWithIntrinsicBounds(R.drawable.humidity_icon,0,0,0)
        textViewWind=view.findViewById(R.id.textViewWind)
        textViewWind.setCompoundDrawablesWithIntrinsicBounds(R.drawable.wind_icon,0,0,0)
        textViewTimeOfDay=view.findViewById(R.id.textViewTimeOfDay)
        getCity(lat.toDouble(),lon.toDouble())
        getData()
        setTimeDescription()
        return view
    }
    companion object {
        var retrofit=RetrofitClass.getRetrofit()
        var AppId = "b6907d289e10d714a6e88b30761fae22"
        var lat:String=""
        var lon:String=""
    }

    private fun setTimeDescription(){
        var data= SimpleDateFormat("HH", Locale.getDefault())
        when (data.format(Date()).toInt()) {
            in 23..4 -> textViewTimeOfDay.text="NIGHT"
            in 4..12 -> textViewTimeOfDay.text="MORNING"
            in 12..17 -> textViewTimeOfDay.text="AFTERNOON"
            in 17..23 -> textViewTimeOfDay.text="EVENING"
        }
    }

    private fun getCity(lat:Double,lon:Double){
        geocoder= Geocoder(context, Locale.getDefault())
        val adresses=geocoder.getFromLocation(lat,lon,1)
        val city= adresses[0].locality
        textViewTitle.text=city
    }

    private fun getData(){
        val queryForData = retrofit?.create(QueryForAPI::class.java)
        val call = queryForData?.getCurrentWeatherData(lat,lon,AppId)
        call?.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                val weatherResponse=response.body()
                textViewWeatherDescription.text=weatherResponse?.weather!![0].description?.toUpperCase()
                textViewHumidity.text="${(weatherResponse.main?.humidity!!).roundToInt()}%"
                textViewDegrees.text= "${(weatherResponse.main?.temp!!).roundToInt()}"
                textViewWind.text=" ${weatherResponse.wind?.speed} M/S"
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.i("error",t.message)
            }
        })
    }
}
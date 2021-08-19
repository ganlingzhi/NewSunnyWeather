package com.example.newsunnyweather.ui.place

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.newsunnyweather.R
import com.example.newsunnyweather.logic.model.Place
import com.example.newsunnyweather.logic.model.Weather
import com.example.newsunnyweather.ui.weather.WeatherActivity

class PlaceAdapter(private val fragment: PlaceFragment, private val placeList: List<Place>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val newType = 1
    private val commonType = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View
        var holder: RecyclerView.ViewHolder
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        if (viewType == commonType) {
            view = layoutInflater.inflate(R.layout.place_item_common, parent, false)
            holder = CommonPlaceViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.place_item_new, parent, false)
            holder = NewPlaceViewHolder(view)
        }
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            val place = placeList[position]
            val intent = Intent(parent.context, WeatherActivity::class.java).apply {
                putExtra("location_lng", place.location.lng)
                putExtra("location_lat", place.location.lat)
                putExtra("place_name", place.name)
                Log.d("placeName", place.name)
            }
            fragment.startActivity(intent)
            fragment.activity?.finish()
            fragment.viewModel.savePlace(place)
        }
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NewPlaceViewHolder) {
            val newHolder: NewPlaceViewHolder = holder
            newHolder.placeName.text = placeList[position].name
            newHolder.newIcon.setImageResource(R.drawable.large_icon)
            newHolder.placeAddress.text = placeList[position].address
        } else {
            val commonHolder: CommonPlaceViewHolder = holder as CommonPlaceViewHolder
            commonHolder.placeAddress.text = placeList[position].address
            commonHolder.placeName.text = placeList[position].name
        }
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if ((position % 2) == 0) {
            newType
        } else {
            commonType
        }
    }
}

class NewPlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val placeName: TextView = itemView.findViewById(R.id.placeName)
    val placeAddress: TextView = itemView.findViewById(R.id.placeAddress)
    val newIcon: ImageView = itemView.findViewById(R.id.newIcon)
}

class CommonPlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val placeName: TextView = itemView.findViewById(R.id.placeName)
    val placeAddress: TextView = itemView.findViewById(R.id.placeAddress)
}

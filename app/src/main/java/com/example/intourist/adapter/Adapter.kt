package com.example.intourist.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.intourist.R
import com.example.intourist.retrofit.model.Tour
import kotlinx.android.synthetic.main.tours.view.*
import java.text.SimpleDateFormat
import java.util.*

class Adapter: RecyclerView.Adapter<Adapter.MyViewHolder>() {
    private var myList = emptyList<Tour>()

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.tours, parent, false))
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentTour = myList[position]
        val dateStr = currentTour.date
        var sdf = SimpleDateFormat("yyyy-MM-dd")
        val date: Date? = sdf.parse(dateStr)
        sdf = SimpleDateFormat("dd.MM.yyyy")

        holder.itemView.title.text = currentTour.title
        holder.itemView.price.text = currentTour.price.toString()
        holder.itemView.date.text = sdf.format(date!!)
        holder.itemView.place.text = currentTour.places

        try {
            Glide.with(holder.itemView.context)
                .load(currentTour.image)
                .into(holder.itemView.image)
        } catch (e: Exception) {
            e.message
        }

        holder.itemView.cardView.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("id", currentTour.id)
                putString("title", currentTour.title)
                putBoolean("isTour", true)
            }
            holder.itemView.findNavController().navigate(R.id.detailFragment, bundle)
        }

    }

    override fun getItemCount(): Int {
        return myList.size
    }

    fun setData(newList: List<Tour>) {
        this.myList = newList
        notifyDataSetChanged()
    }
}
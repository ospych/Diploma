package com.example.intourist.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.intourist.R
import com.example.intourist.room.data.TourRoom
import kotlinx.android.synthetic.main.tour_fav.view.*
import java.text.SimpleDateFormat
import java.util.*

class AdapterFavourite : RecyclerView.Adapter<AdapterFavourite.MyViewHolder>() {

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<TourRoom>() {
        override fun areItemsTheSame(oldItem: TourRoom, newItem: TourRoom): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TourRoom, newItem: TourRoom): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.tour_fav,
                        parent,
                        false
                )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((TourRoom) -> Unit)? = null

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val tour = differ.currentList[position]
        holder.itemView.apply {
            val dateStr = tour.date
            var sdf = SimpleDateFormat("yyyy-MM-dd")
            val date: Date? = sdf.parse(dateStr)
            sdf = SimpleDateFormat("dd.MM.yyyy")
            Glide.with(this).load(tour.image).into(image)
            titleFav.text = tour.title
            priceFav.text = tour.price.toString()
            dateFav.text = sdf.format(date)
            placeFav.text = tour.places

            Log.v("AdapterInside", tour.title)

            setOnClickListener {
                onItemClickListener?.let { it(tour) }
            }
        }
    }

    fun setOnItemClickListener(listener: (TourRoom) -> Unit) {
        onItemClickListener = listener
    }
}

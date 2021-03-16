package com.example.intourist.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.intourist.R
import com.example.intourist.ui.SingleTourActivity
import com.example.intourist.retrofit.model.Tour
import kotlinx.android.synthetic.main.tours.view.*

class Adapter: RecyclerView.Adapter<Adapter.MyViewHolder>() {
    private var myList = emptyList<Tour>()

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.tours, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentTour = myList[position]
        holder.itemView.title.text = currentTour.title
        holder.itemView.price.text = currentTour.price.toString()
        holder.itemView.date.text = currentTour.date
        holder.itemView.place.text = currentTour.places

        try {
            Glide.with(holder.itemView.context)
                .load(currentTour.image)
                .into(holder.itemView.image)
        } catch (e: Exception) {
            e.message
        }

        holder.itemView.cardView.setOnClickListener {
            val id = currentTour.id
            val intent = Intent(holder.itemView.context, SingleTourActivity::class.java)
            intent.putExtra("id", id)
            holder.itemView.context.startActivity(intent)
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
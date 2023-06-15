package com.bangkit.freshcanapp.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.freshcanapp.R
import com.bangkit.freshcanapp.data.remote.response.HistoryItemResponse
import com.bangkit.freshcanapp.data.remote.response.PayloadItem
import com.bangkit.freshcanapp.ui.view.detail.DetailActivity
import com.bangkit.freshcanapp.ui.view.history.HistoryFragment
import com.bumptech.glide.Glide

class HistoryAdapter (private val listHistory: List<PayloadItem>) : RecyclerView.Adapter<HistoryAdapter.ListViewHolder>() {
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.imageViewHistory)
        val tvName: TextView = itemView.findViewById(R.id.bahanHistoryText)
        val tvDate: TextView = itemView.findViewById(R.id.tanggalHistoryText)
        val tvCondition: TextView = itemView.findViewById(R.id.freshnessHistoryText)
//        val tvDescription: TextView = itemView.findViewById(R.id.tv_item_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (condition, informationName,createdDate,percentage,userEmail,id,urlImage) = listHistory[position]
//        holder.imgPhoto.setImageResource(photo)
        Glide.with(holder.itemView.context)
            .load(urlImage) // URL Gambar
            .into(holder.imgPhoto)
        holder.tvName.text = informationName
//        holder.tvDescription.text = description
        holder.tvDate.text = createdDate
        if(condition.equals("Rotten")){
            holder.tvCondition.text = condition
            holder.tvCondition.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.rotten_freshcan))
        }else{
            holder.tvCondition.text = condition
            holder.tvCondition.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.green_freshcan))
        }
        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra("id", id.toString())
            intentDetail.putExtra("informationName", informationName)
            intentDetail.putExtra("previousPage", "History")
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    override fun getItemCount(): Int {
        return listHistory.size
    }
}
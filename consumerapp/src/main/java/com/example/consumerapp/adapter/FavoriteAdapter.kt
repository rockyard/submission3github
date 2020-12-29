package com.example.consumerapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.consumerapp.CustomOnItemClickListener
import com.example.consumerapp.DetailActivity
import com.example.consumerapp.R
import com.example.consumerapp.data.Favorite
import kotlinx.android.synthetic.main.item_row_git.view.*
import java.util.ArrayList

class FavoriteAdapter (private val activity: Activity) :
        RecyclerView.Adapter<FavoriteAdapter.NoteViewHolder>(){
    var listFavorite = ArrayList<Favorite>()
        set(listFavorite) {
            if (listFavorite.size > 0) {
                this.listFavorite.clear()
            }
            this.listFavorite.addAll(listFavorite)

            notifyDataSetChanged()
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_git, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }

    override fun getItemCount(): Int = this.listFavorite.size

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(fav: Favorite) {
            with(itemView) {
                Glide.with(itemView.context)
                        .load(fav.avatar)
                        .apply(RequestOptions().override(100, 100))
                        .into(itemView.avatar)
                username.text = fav.username
                user_name.text = fav.name
                tv_company.text = fav.company.toString().trim()
                tv_location.text = fav.location.toString().trim()
                followers.text = fav.followers.toString().trim()
                following.text = fav.following.toString().trim()
                itemView.setOnClickListener(
                        CustomOnItemClickListener(
                                adapterPosition,
                                object : CustomOnItemClickListener.OnItemClickCallback {
                                    override fun onItemClicked(view: View, position: Int) {
                                        val intent = Intent(activity, DetailActivity::class.java)
                                        intent.putExtra(DetailActivity.EXTRA_POSITION, position)
                                        intent.putExtra(DetailActivity.EXTRA_NOTE, fav)
                                        activity.startActivity(intent)
                                    }
                                }
                        )
                )
            }
        }
    }
}
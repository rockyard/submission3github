package com.example.githubusersubmission.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubusersubmission.R
import com.example.githubusersubmission.data.DataUsers
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_row_git.view.*

var followingFilterList = ArrayList<DataUsers>()

class ListDataFollowingAdapter(listData: ArrayList<DataUsers>):
      RecyclerView.Adapter<ListDataFollowingAdapter.ListDataHolder>(){

    init {
        followingFilterList = listData
    }

    inner class ListDataHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var imageAvatar : CircleImageView = itemView.avatar
        var name: TextView = itemView.user_name
        var username: TextView = itemView.username
        var company : TextView = itemView.tv_company
        var location : TextView = itemView.tv_location
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback

    }

    interface OnItemClickCallback  {
        fun onItemClicked(DataUsers: DataUsers)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListDataHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_row_git, viewGroup, false)
        val gitusers = ListDataHolder(view)
        mcontext = viewGroup.context
        return gitusers
    }

    override fun getItemCount(): Int {
        return followersFilterList.size
    }

    override fun onBindViewHolder(holder: ListDataHolder, position: Int) {
        val data = followingFilterList[position]
        Glide.with(holder.itemView.context)
            .load(data.avatar)
            .apply(RequestOptions().override(100, 100))
            .into(holder.imageAvatar)
        holder.name.text = data.name
        holder.username.text = data.username
        holder.company.text = data.company
        holder.location.text = data.location
        holder.itemView.setOnClickListener{

        }
    }
}
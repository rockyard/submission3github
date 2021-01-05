package com.example.consumerapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.consumerapp.R
import com.example.consumerapp.data.DataUsers
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_row_git.view.*


var followersFilterList = ArrayList<DataUsers>()
lateinit var mcontext: Context


class ListDataFollowersAdapter (listData: ArrayList<DataUsers>) :
        RecyclerView.Adapter<ListDataFollowersAdapter.ListDataHolder>(){

    init {
        followersFilterList = listData
    }

    inner class ListDataHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var imageAvatar: CircleImageView = itemView.avatar
        var name: TextView = itemView.user_name
        var username : TextView = itemView.username
        var company : TextView = itemView.tv_company
        var location : TextView = itemView.tv_location

    }

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback (onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked (DataUsers: DataUsers)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListDataHolder {
        val view : View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_row_git, viewGroup, false)
        val gitusers = ListDataHolder(view)
        mcontext = viewGroup.context
        return gitusers
    }

    override fun getItemCount(): Int {
        return followersFilterList.size
    }

    override fun onBindViewHolder(holder: ListDataHolder, position: Int) {
        val data = followersFilterList[position]
        Glide.with(holder.itemView.context)
            .load(data.avatar)
            .apply(RequestOptions().override(100,100))
            .into(holder.imageAvatar)
        holder.name.text = data.name
        holder.username.text = data.username
        holder.company.text = data.company
        holder.location.text = data.location
        holder.itemView.setOnClickListener{

        }
    }
}
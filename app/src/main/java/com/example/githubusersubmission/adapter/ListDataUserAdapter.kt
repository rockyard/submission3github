package com.example.githubusersubmission.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubusersubmission.DetailActivity
import com.example.githubusersubmission.R
import com.example.githubusersubmission.data.DataUsers
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_row_git.view.*
import java.util.*
import kotlin.collections.ArrayList


var userFilterList = ArrayList<DataUsers>()
lateinit var mcontext: Context



class ListDataUserAdapter(private var listData: ArrayList<DataUsers>):
        RecyclerView.Adapter<ListDataUserAdapter.ListDataHolder>(), Filterable{

    init {
        userFilterList = listData
    }

    inner class ListDataHolder(itemView: View): RecyclerView.ViewHolder (itemView){
        var imageAvatar : CircleImageView = itemView.avatar
        var name: TextView = itemView.user_name
        var username : TextView = itemView.username
        var company: TextView = itemView.tv_company
        var location : TextView = itemView.tv_location
    }

   private lateinit var onItemClickCallback : OnItemClickCallback

   fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(dataUsers: DataUsers)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListDataHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_row_git, viewGroup, false)
        val gitusers = ListDataHolder(view)
        mcontext = viewGroup.context
        return gitusers
    }

    override fun onBindViewHolder(holder: ListDataHolder, position: Int) {
        val data = userFilterList[position]
        Glide.with(holder.itemView.context)
                .load(data.avatar)
                .apply(RequestOptions().override(100,100))
                .into(holder.imageAvatar)
        holder.name.text = data.name
        holder.username.text = data.username
        holder.company.text = data.company.toString().trim()
        holder.location.text = data.location.toString().trim()
        holder.itemView.setOnClickListener {
            val dataUser = DataUsers(
                    data.username,
                    data.name,
                    data.avatar,
                    data.company,
                    data.location,
                    data.repository,
                    data.followers,
                    data.following
            )
            val intentDetail = Intent(mcontext, DetailActivity::class.java)
            intentDetail.putExtra(DetailActivity.EXTRA_DATA, dataUser)
            intentDetail.putExtra(DetailActivity.EXTRA_FAV, dataUser )
            mcontext.startActivity(intentDetail)
        }
    }

    override fun getItemCount(): Int {
        return userFilterList.size
    }

    override fun getFilter(): Filter {
       return object : Filter(){
           override fun performFiltering(constraint: CharSequence): FilterResults {
               val charSearch = constraint.toString()
               userFilterList = if (charSearch.isEmpty()){
                   listData
               }else {
                   val resultList = ArrayList<DataUsers>()
                   for (row in userFilterList){
                       if ((row.username.toString().toLowerCase(Locale.ROOT)
                                       .contains(charSearch.toLowerCase(Locale.ROOT)))
                       ){
                           resultList.add(
                                   DataUsers(
                                           row.username,
                                           row.name,
                                           row.avatar,
                                           row.company,
                                           row.location,
                                           row.repository,
                                           row.followers,
                                           row.following
                                   )
                           )
                       }
                   }
                   resultList
               }
               val filterResults = FilterResults()
               filterResults.values = userFilterList
               return filterResults
           }

           @Suppress("UNCHECKED_CAST")
           override fun publishResults(constraint: CharSequence, results: FilterResults) {
               userFilterList = results.values as ArrayList<DataUsers>
               notifyDataSetChanged()
           }
       }
    }
}
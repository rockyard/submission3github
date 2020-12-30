package com.example.consumerapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.service.autofill.UserData
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumerapp.DetailActivity
import com.example.consumerapp.R
import com.example.consumerapp.adapter.ListDataFollowersAdapter
import com.example.consumerapp.adapter.ListDataFollowingAdapter
import com.example.consumerapp.adapter.followersFilterList
import com.example.consumerapp.data.DataUsers
import com.example.consumerapp.data.Favorite
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_followers.*
import org.json.JSONArray
import org.json.JSONObject


class FollowersFragment : Fragment() {

    companion object {
        private val TAG = FollowersFragment::class.java.simpleName
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_NOTE = "extra_note"
    }

    private var listData: ArrayList<DataUsers> = ArrayList()
    private lateinit var adapter: ListDataFollowersAdapter
    private var favorites: Favorite? = null
    private lateinit var dataFavorite: Favorite
    private lateinit var dataUsers: DataUsers




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ListDataFollowersAdapter(listData)
        listData.clear()

        favorites = activity!!.intent.getParcelableExtra(DetailActivity.EXTRA_NOTE)
        if (favorites != null) {
            dataFavorite = activity!!.intent.getParcelableExtra<Favorite>(EXTRA_NOTE) as Favorite
            getDataGit(dataFavorite.username.toString())
        } else {
            dataUsers = activity!!.intent.getParcelableExtra<DataUsers>(EXTRA_DATA) as DataUsers
            getDataGit(dataUsers.username.toString())
        }
    }

    private fun getDataGit (id: String){
        progressBarFollowers.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token e915e1805aeb0954a49e0e0f63ebc23802a81e99")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$id/followers"
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray) {
                progressBarFollowers.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()){
                        val jsonObject = jsonArray.getJSONObject(i)
                        val username: String = jsonObject.getString("login")
                        getDataGitDetail(username)
                    }
                }catch (e: Exception){
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray, error: Throwable) {
                progressBarFollowers.visibility = View.INVISIBLE
                val errorMessage = when(statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun getDataGitDetail (id: String){
        progressBarFollowers.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token e915e1805aeb0954a49e0e0f63ebc23802a81e99")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$id"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray) {
                progressBarFollowers.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonObject = JSONObject(result)
                    val username: String? = jsonObject.getString("login").toString()
                    val name: String? = jsonObject.getString("name").toString()
                    val avatar: String? = jsonObject.getString("avatar_url").toString()
                    val company: String? = jsonObject.getString("company").toString()
                    val location: String? = jsonObject.getString("location").toString()
                    val repository:String? = jsonObject.getString("public_repos")
                    val followers: String? = jsonObject.getString("followers")
                    val following: String? = jsonObject.getString("following")
                    listData.add(
                            DataUsers(
                                    username,
                                    name,
                                    avatar,
                                    company,
                                    location,
                                    repository,
                                    followers,
                                    following
                            )
                    )
                    showRecyclerList()
                }catch (e: Exception){
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray, error: Throwable) {
                progressBarFollowers.visibility = View.INVISIBLE
                val errorMessage = when(statusCode){
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun showRecyclerList() {
        recycleViewFollowers.layoutManager = LinearLayoutManager(activity)
        val listDataAdapter = ListDataFollowersAdapter(followersFilterList)
        recycleViewFollowers.adapter = adapter

        listDataAdapter.setOnItemClickCallback(object :
        ListDataFollowersAdapter.OnItemClickCallback {
            override fun onItemClicked(DataUsers: DataUsers){
                //DO NOTHING
            }
        })
    }
}
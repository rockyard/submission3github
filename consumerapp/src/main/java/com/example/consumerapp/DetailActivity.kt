package com.example.consumerapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.consumerapp.adapter.ViewPagerDetailAdapter
import com.example.consumerapp.data.Favorite
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity(){

    companion object{

        const val EXTRA_NOTE = "extra_note"
        const val EXTRA_POSITION = "extra_position"
    }

    private lateinit var imageAvatar: String

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setDataObject()
        viewPagerConfig()

    }

    private fun viewPagerConfig(){
        val viewPagerDetailAdapter = ViewPagerDetailAdapter(this, supportFragmentManager)
        view_pager.adapter = viewPagerDetailAdapter
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f
    }

    private fun setActionBarTitle(title:String){
        if (supportActionBar != null){
            supportActionBar!!.title =title
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDataObject() {
        val favoriteUser = intent.getParcelableExtra<Favorite>(EXTRA_NOTE) as Favorite
        favoriteUser.name?.let { setActionBarTitle(it) }
        detail_name.text = favoriteUser.name
        detail_username.text = favoriteUser.username
        detail_company.text = favoriteUser.company
        detail_location.text = favoriteUser.location
        detail_repo.text = favoriteUser.repository
        followerss.text = favoriteUser.followers
        followings.text = favoriteUser.following
        Glide.with(this)
                .load(favoriteUser.avatar)
                .into(avatars)
        imageAvatar = favoriteUser.avatar.toString()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_change_settings -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
            R.id.action_change_notification -> {
                val mIntent = Intent(this, NotificationSettings::class.java)
                startActivity(mIntent)
            }
            R.id.action_favorite -> {
                val mIntent = Intent(this, MainActivity::class.java)
                startActivity(mIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
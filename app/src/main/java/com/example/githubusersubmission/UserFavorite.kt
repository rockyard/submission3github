package com.example.githubusersubmission

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusersubmission.adapter.FavoriteAdapter
import com.example.githubusersubmission.data.Favorite
import com.example.githubusersubmission.db.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.example.githubusersubmission.helper.MappingHelper
import kotlinx.android.synthetic.main.user_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch



class UserFavorite : AppCompatActivity() {
private lateinit var adapter: FavoriteAdapter

companion object {
    private const val EXTRA_STATE = "EXTRA_STATE"
}

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.user_favorite)
    setActionBarTitle()

    recycleViewFav.layoutManager = LinearLayoutManager(this)
    recycleViewFav.setHasFixedSize(true)
    adapter = FavoriteAdapter(this)
    recycleViewFav.adapter = adapter

    val handlerThread = HandlerThread("DataObserver")
    handlerThread.start()
    val handler = Handler(handlerThread.looper)
    val myObserver = object : ContentObserver(handler) {
        override fun onChange(self: Boolean) {
            loadNotesAsync()
        }
    }

    contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

    if (savedInstanceState == null) {
        loadNotesAsync()
    } else {
        val list = savedInstanceState.getParcelableArrayList<Favorite>(EXTRA_STATE)
        if (list != null) {
            adapter.listFavorite = list
        }
    }
}


private fun setActionBarTitle() {
    if (supportActionBar != null) {
        supportActionBar?.title = "Favorite Users"
    }
}


private fun loadNotesAsync() {
    GlobalScope.launch(Dispatchers.Main) {
        progressBarFav.visibility = View.VISIBLE
        val deferredNotes = async(Dispatchers.IO) {
            val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
            MappingHelper.mapCursorToArrayList(cursor)
        }
        val favData = deferredNotes.await()
        progressBarFav.visibility = View.INVISIBLE
        if (favData.size > 0) {
            adapter.listFavorite = favData
        } else {
            adapter.listFavorite = ArrayList()
            showSnackbarMessage()
        }
    }
}

override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavorite)
}

private fun showSnackbarMessage() {
    Toast.makeText(this, getString(R.string.empty_favorite), Toast.LENGTH_SHORT).show()
}

// run this func when open again for refresh data
override fun onResume() {
    super.onResume()
    loadNotesAsync()
    }
}
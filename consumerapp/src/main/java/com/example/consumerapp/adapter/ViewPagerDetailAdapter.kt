package com.example.consumerapp.adapter

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.consumerapp.fragment.FollowersFragment
import com.example.consumerapp.fragment.FollowingFragment
import com.example.consumerapp.R

class ViewPagerDetailAdapter (private val mContext: Context, fm: FragmentManager):
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

    companion object {
        @StringRes
        private val tabTitle = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    override fun getItem(position: Int): Fragment {
        var fragment : Fragment? = null
        when(position){
            0 -> fragment = FollowersFragment()
            1 -> fragment = FollowingFragment()
        }

        return fragment as Fragment
    }

    override fun getCount(): Int {

        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(tabTitle[position])
    }
}
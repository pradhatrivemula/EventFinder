package com.example.eventfinder.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.eventfinder.Fragment.FavouriteFragment
import com.example.eventfinder.Fragment.SearchFragment

class PagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return SearchFragment()
            1 -> return FavouriteFragment()
        }
        return SearchFragment()
    }

    override fun getItemCount(): Int {
        return 2
    }
}
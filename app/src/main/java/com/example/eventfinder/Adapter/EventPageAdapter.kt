package com.example.eventfinder.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class EventPageAdapter constructor(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    private val arrayList: ArrayList<Fragment> = ArrayList()
    fun addFragment(fragment: Fragment) {
        arrayList.add(fragment)
    }

    public override fun getItemCount(): Int {
        return arrayList.size
    }

    public override fun createFragment(position: Int): Fragment {
        return arrayList.get(position)
    }
}
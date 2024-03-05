package com.example.eventfinder

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager2.widget.ViewPager2
import com.example.eventfinder.Activity.LogoutActivity
import com.example.eventfinder.Adapter.PagerAdapter
import com.example.eventfinder.Fragment.ResultsFragment
import com.example.eventfinder.Fragment.SearchFragment
import com.example.eventfinder.Listener.OnFragmentInteractionListener
import com.example.eventfinder.Util.EventSpinner
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy


class MainActivity constructor() : AppCompatActivity(), OnFragmentInteractionListener {
    private var eventSpinner: EventSpinner? = null
    private var tabLayout: TabLayout? = null
    private var viewPager2: ViewPager2? = null
    private lateinit var toolbar: Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tabLayout = findViewById(R.id.tab_layout)
        viewPager2 = findViewById(R.id.view_pager)

        toolbar = findViewById(R.id.toolbar)
        toolbar.inflateMenu(R.menu.main_menu)

        toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId === R.id.action_settings) {
                val intent = Intent(this, LogoutActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            false
        }

        val pagerAdapter: PagerAdapter = PagerAdapter(this)
        viewPager2?.adapter = pagerAdapter
        tabLayout?.let {
            viewPager2?.let { it1 ->
                TabLayoutMediator(
                    it, it1,
                    TabConfigurationStrategy { tab: TabLayout.Tab, position: Int ->
                        when (position) {
                            0 -> tab.text = "SEARCH"
                            1 -> tab.text = "FAVORITES"
                        }
                    }
                ).attach()
            }
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main_menu, menu)
//        return true
//    }

    public override fun changeFragment(id: Int) {
        if (id == 1) {
            val rf: ResultsFragment = ResultsFragment()
            val ft: FragmentTransaction = getSupportFragmentManager().beginTransaction()
            ft.replace(R.id.relativeResultLayout, rf)
            ft.commit()
        }
        if (id == 2) {
            val sf: SearchFragment = SearchFragment()
            val ft: FragmentTransaction = getSupportFragmentManager().beginTransaction()
            ft.add(R.id.relativeResultLayout, SearchFragment())
            ft.commit()
        }
    }

    fun showProgress(show: Boolean) {
        if (eventSpinner == null) eventSpinner = EventSpinner()
        if (show) eventSpinner!!.show(
            getSupportFragmentManager(),
            MainActivity::class.java.getName()
        ) else eventSpinner!!.dismissAllowingStateLoss()
    }

    companion object {
        private val PERMISSION_REQUEST_CODE: Int = 1001
    }
}
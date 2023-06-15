package com.bangkit.freshcanapp.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bangkit.freshcanapp.ui.view.history.HistoryFragment
import com.bangkit.freshcanapp.ui.view.main.HomeFragment
import kotlinx.coroutines.CoroutineScope

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var username: String? = null

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = HistoryFragment()
            1 -> fragment = HomeFragment()
        }
        return fragment as Fragment
    }

}
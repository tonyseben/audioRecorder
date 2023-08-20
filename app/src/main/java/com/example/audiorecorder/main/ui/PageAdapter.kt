package com.example.audiorecorder.main.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.audiorecorder.record.ui.RecordFragment
import com.example.audiorecorder.record.ui.HistoryFragment

class PageAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> HistoryFragment()
            else -> RecordFragment()
        }
    }
}
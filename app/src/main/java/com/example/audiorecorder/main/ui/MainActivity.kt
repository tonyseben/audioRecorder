package com.example.audiorecorder.main.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.audiorecorder.R
import com.example.audiorecorder.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var pageAdapter: PageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pageAdapter = PageAdapter(supportFragmentManager, lifecycle)
        binding.initViews()
    }

    private fun ActivityMainBinding.initViews() {
        viewPager.adapter = pageAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                1 -> getString(R.string.tabMore)
                else -> getString(R.string.tabMessages)
            }
        }.attach()
    }
}
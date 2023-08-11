package com.mvi_state_automation.example.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mvi_state_automation.example.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
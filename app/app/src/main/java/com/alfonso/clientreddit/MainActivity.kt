package com.alfonso.clientreddit

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.GestureDetectorCompat
import com.alfonso.clientreddit.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

import com.alfonso.clientreddit.databinding.ActivityMainBinding
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel : MainViewModel by viewModels()
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
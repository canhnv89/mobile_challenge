package com.example.mobilechallenge.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mobilechallenge.R
import com.example.mobilechallenge.constant.FragmentNavigator
import com.example.mobilechallenge.databinding.ActivityMainBinding
import com.example.mobilechallenge.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: $savedInstanceState")
        setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.root, PayFragment.newInstance())
                .commit()
        }
        lifecycleScope.launchWhenCreated {
            viewModel = ViewModelProvider(this@MainActivity)[MainViewModel::class.java]
            viewModel.getNavigatorLiveData().observe(this@MainActivity) {
                navigateToFragment(it)
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }

    private fun navigateToFragment(navigator: FragmentNavigator) {
        Log.d(TAG, "navigateToFragment: $navigator")
        val transaction = supportFragmentManager.beginTransaction().setCustomAnimations(
            R.anim.slide_in_right_to_left,
            R.anim.slide_out_right_to_left
        )
        when (navigator) {
            FragmentNavigator.PAY_CONFIRM_FRAGMENT -> transaction.replace(R.id.root, PayConfirm3dsFragment.newInstance()).commit()
            FragmentNavigator.SUCCESS_FRAGMENT -> transaction.replace(R.id.root, SuccessFragment.newInstance()).commit()
            FragmentNavigator.FAILURE_FRAGMENT -> transaction.replace(R.id.root, FailureFragment.newInstance()).commit()
            else -> {}
        }
    }

}
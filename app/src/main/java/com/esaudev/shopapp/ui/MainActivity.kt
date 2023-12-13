package com.esaudev.shopapp.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.esaudev.shopapp.R
import com.esaudev.shopapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (Build.VERSION.SDK_INT < 31) setTheme(R.style.Theme_ShopApp)
        val splashScreen = installSplashScreen().apply {
            setKeepOnScreenCondition {
                true
            }
        }

        subscribeViewModel()
        loadAppView()

        viewModel.getUserToken()
        viewModel.hasSplashFinished.observe(this) {
            splashScreen.setKeepOnScreenCondition {
                false
            }
        }
    }

    private fun subscribeViewModel() {
        viewModel.userToken.observe(this) { userToken ->
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.fcvMainActivity) as NavHostFragment
            val navController = navHostFragment.navController
            val navGraph =
                findNavController(R.id.fcvMainActivity).navInflater.inflate(R.navigation.main_nav_graph)

            if (userToken.isNotEmpty()) {
                navGraph.setStartDestination(R.id.homeFragment)
            } else {
                navGraph.setStartDestination(R.id.logInFragment)
            }
            navController.graph = navGraph
            viewModel.splashFinished()
        }
    }

    private fun loadAppView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
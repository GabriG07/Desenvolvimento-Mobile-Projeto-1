package com.example.projeto1

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.projeto1.navigation.AppNavGraph
import com.example.projeto1.ui.theme.ColorBackground
import com.example.projeto1.ui.theme.Projeto1Theme
import com.example.projeto1.ui.viewmodel.OnboardingViewModel

class MainActivity : ComponentActivity() {

    val onboardingViewModel: OnboardingViewModel by viewModels<OnboardingViewModel>()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onboardingViewModel.provideActivity(this)
        enableEdgeToEdge()
        setContent {
            Projeto1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = ColorBackground
                ) {
                    AppNavGraph(onboardingViewModel, activity = this)
                }
            }
        }
    }
}
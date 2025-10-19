package com.luis.soundify.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.luis.soundify.BuildConfig
import com.luis.soundify.R
import com.luis.soundify.SpotifyAuthManager
import com.luis.soundify.presentation.logIn.LogInScreen
import com.luis.soundify.presentation.logIn.LogInViewModel
import com.luis.soundify.presentation.theme.SoundifyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var spotifyAuthManager: SpotifyAuthManager
    private val logInViewModel: LogInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Soundify)

        spotifyAuthManager = SpotifyAuthManager(this)

        enableEdgeToEdge()
        installSplashScreen()

        setContent {
            SoundifyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        LogInScreen(
                            viewModel = logInViewModel,
                            onGetStartedClick = {
                                logInViewModel.startAuthorization()
                                spotifyAuthManager.startAuthorization()
                            })
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.data?.let { uri ->
            if (uri.toString().startsWith(BuildConfig.REDIRECT_URI)) {
                val code = uri.getQueryParameter("code")
                val error = uri.getQueryParameter("error")
                if (code != null) {
                    logInViewModel.requestAccessToken(
                        code = code,
                        codeVerifier = spotifyAuthManager.getCodeVerifier()
                    )
                } else if (error != null) {
                    logInViewModel.authorizationFail()
                }
            }
        }
    }
}
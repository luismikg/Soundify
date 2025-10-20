package com.luis.soundify.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.luis.soundify.BuildConfig
import com.luis.soundify.R
import com.luis.soundify.SpotifyAuthManager
import com.luis.soundify.presentation.logIn.LogInViewModel
import com.luis.soundify.presentation.navigation.NavigationComponent
import com.luis.soundify.presentation.theme.SoundifyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var spotifyAuthManager: SpotifyAuthManager
    private val logInViewModel: LogInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Soundify)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        spotifyAuthManager = SpotifyAuthManager(this)

        enableEdgeToEdge()
        installSplashScreen()

        setContent {
            SoundifyTheme {
                val view = LocalView.current
                SideEffect {
                    val window = (view.context as Activity).window
                    WindowCompat
                        .getInsetsController(window, view)
                        .isAppearanceLightStatusBars = false
                    WindowCompat
                        .getInsetsController(window, view)
                        .isAppearanceLightNavigationBars = false
                }
                Column(modifier = Modifier.fillMaxSize()) {
                    NavigationComponent(
                        logInViewModel = logInViewModel,
                        onGetStartedClick = {
                            logInViewModel.startAuthorization()
                            spotifyAuthManager.startAuthorization()
                        }
                    )
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
package com.luis.soundify

import android.app.Activity
import android.content.Intent
import android.util.Base64
import androidx.core.net.toUri
import java.security.MessageDigest
import java.security.SecureRandom

class SpotifyAuthManager(private val activity: Activity) {

    private lateinit var codeVerifier: String
    private val SCOPES = arrayOf(
        "user-read-private",
        "user-read-email",
        "user-read-playback-state",
        "user-modify-playback-state",
        "user-read-currently-playing",
        "playlist-read-private",
        "playlist-read-collaborative",
        "playlist-modify-public",
        "playlist-modify-private"
    )

    fun startAuthorization() {
        codeVerifier = generateCodeVerifier()
        val codeChallenge = generateCodeChallenge(codeVerifier)
        val uri = BuildConfig.AUTHORIZATION_ENDPOINT.toUri()
            .buildUpon()
            .appendQueryParameter("client_id", BuildConfig.CLIENT_ID)
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("redirect_uri", BuildConfig.REDIRECT_URI)
            .appendQueryParameter("code_challenge_method", "S256")
            .appendQueryParameter("code_challenge", codeChallenge)
            .appendQueryParameter("scope", SCOPES.joinToString(" "))
            .build()

        val intent = Intent(Intent.ACTION_VIEW, uri)
        activity.startActivity(intent)
    }

    private fun generateCodeVerifier(): String {
        val bytes = ByteArray(32)
        SecureRandom().nextBytes(bytes)
        return Base64.encodeToString(bytes, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
    }

    private fun generateCodeChallenge(verifier: String): String {
        val bytes = verifier.toByteArray(Charsets.US_ASCII)
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return Base64.encodeToString(digest, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
    }

    fun getCodeVerifier(): String = codeVerifier
}

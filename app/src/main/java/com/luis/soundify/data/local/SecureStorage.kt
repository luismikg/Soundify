package com.luis.soundify.data.local

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.luis.soundify.data.DataConstants

class SecureStorage(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        DataConstants.NAME_FILE_SECURE,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    //Token
    fun saveToken(token: String) {
        sharedPreferences.edit {
            putString(DataConstants.KEY_ACCESS_TOKEN, token)
        }
    }

    fun getToken(): String? {
        return sharedPreferences.getString(DataConstants.KEY_ACCESS_TOKEN, null)
    }

    fun clearToken() {
        sharedPreferences.edit {
            remove(DataConstants.KEY_ACCESS_TOKEN)
        }
    }

    //Refresh token
    fun saveRefreshToken(token: String) {
        sharedPreferences.edit {
            putString(DataConstants.KEY_ACCESS_REFRESH_TOKEN, token)
        }
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString(DataConstants.KEY_ACCESS_REFRESH_TOKEN, null)
    }

    fun clearRefreshToken() {
        sharedPreferences.edit {
            remove(DataConstants.KEY_ACCESS_REFRESH_TOKEN)
        }
    }
}
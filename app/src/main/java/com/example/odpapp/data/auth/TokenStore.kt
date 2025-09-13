package com.example.odpapp.data.auth

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.Instant

private val Context.ds by preferencesDataStore("auth_prefs")

object TokenStore {
    private val KEY_TOKEN = stringPreferencesKey("api_token")
    private val KEY_EXPIRES_AT = longPreferencesKey("expires_at_epoch")

    suspend fun save(context: Context, token: String, expiresAtEpochSeconds: Long? = null) {
        context.ds.edit {
            it[KEY_TOKEN] = token
            if (expiresAtEpochSeconds != null) it[KEY_EXPIRES_AT] = expiresAtEpochSeconds
        }
    }

    suspend fun clear(context: Context) {
        context.ds.edit { it.clear() }
    }

    suspend fun getToken(context: Context): String? =
        context.ds.data.map { it[KEY_TOKEN] }.first()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun isExpired(context: Context): Boolean =
        context.ds.data.map {
            val exp = it[KEY_EXPIRES_AT] ?: 0L
            exp != 0L && Instant.now().epochSecond >= exp
        }.first()
}

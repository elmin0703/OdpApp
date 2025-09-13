package com.example.odpapp.network

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import com.example.odpapp.data.auth.TokenStore
import com.example.odpapp.core.events.TokenEvents

/** Dodaje Authorization: Bearer <token> ako postoji u DataStore */
class AuthHeaderInterceptor(private val appContext: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { TokenStore.getToken(appContext) }
        val req = chain.request().let { r ->
            if (!token.isNullOrBlank())
                r.newBuilder().addHeader("Authorization", "Bearer $token").build()
            else r
        }
        return chain.proceed(req)
    }
}

/** Hvala 401 + opcioni preflight ako imamo expiresAt */
class TokenExpiryGuardInterceptor(private val appContext: Context) : Interceptor {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun intercept(chain: Interceptor.Chain): Response {
        // Opcionalno: ako čuvaš expiresAt u DataStore, odmah javi UI
        runBlocking { if (TokenStore.isExpired(appContext)) TokenEvents.emitExpired() }

        val resp = chain.proceed(chain.request())

        if (resp.code == 401) {
            TokenEvents.emitExpired()
        }
        return resp
    }
}

package com.example.odpapp.util

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.example.odpapp.MainActivity

object AppRestarter {
    /** Potpuni restart aplikacije: čisti task + pokreće MainActivity iz nule. */
    fun restartApp(context: Context) {
        // Kreira novi root task s MainActivity kao start destinacijom
        val intent = Intent.makeRestartActivityTask(
            ComponentName(context, MainActivity::class.java)
        )
        context.startActivity(intent)

        // Ako smo u Activity-ju, zatvori trenutni prozor
        if (context is Activity) {
            context.finish()
        }

        // Sigurno ugasi trenutni proces (sprječava "stare" singletons)
        Runtime.getRuntime().exit(0)
        // Alternativa: android.os.Process.killProcess(android.os.Process.myPid())
    }
}

package com.jefisu.chatapp.features_auth.domain.use_cases

import android.content.SharedPreferences
import com.jefisu.chatapp.core.util.SimpleResource
import com.jefisu.chatapp.core.util.requestCatch
import com.jefisu.chatapp.features_auth.domain.repository.AuthRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SignIn(
    private val repository: AuthRepository,
    private val prefs: SharedPreferences
) {
    suspend operator fun invoke(email: String, password: String): SimpleResource {
        return requestCatch {
            val user = repository.signIn(email, password)
            val parsedUser = Json.encodeToString(user)
            prefs.edit()
                .putString("user", parsedUser)
                .apply()
        }
    }
}
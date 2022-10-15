package com.jefisu.chatapp.features_auth.domain.use_cases

import android.content.SharedPreferences
import com.jefisu.chatapp.core.util.SimpleResource
import com.jefisu.chatapp.core.util.requestCatch
import com.jefisu.chatapp.features_auth.domain.repository.AuthRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SignUp(
    private val repository: AuthRepository,
    private val prefs: SharedPreferences
) {
    suspend operator fun invoke(
        username: String,
        email: String,
        password: String
    ): SimpleResource {
        return requestCatch {
            val user = repository.signUp(username, email, password)
            val parsedUser = Json.encodeToString(user)
            prefs.edit()
                .putString("user", parsedUser)
                .apply()
        }
    }
}
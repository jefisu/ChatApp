package com.jefisu.chatapp.features_auth.domain.use_cases

import com.jefisu.chatapp.core.data.model.User
import com.jefisu.chatapp.core.util.Resource
import com.jefisu.chatapp.core.util.requestCatch
import com.jefisu.chatapp.features_auth.domain.repository.AuthRepository

class SignIn(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Resource<User> {
        return requestCatch {
            repository.signIn(email, password)
        }
    }
}
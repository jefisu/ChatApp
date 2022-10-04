package com.jefisu.chatapp.features_auth.domain.use_cases

import com.jefisu.chatapp.core.data.model.User
import com.jefisu.chatapp.core.util.Resource
import com.jefisu.chatapp.core.util.requestCatch
import com.jefisu.chatapp.features_auth.domain.repository.AuthRepository

class SignUp(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        username: String,
        email: String,
        password: String
    ): Resource<User> {
        return requestCatch {
            repository.signUp(username, email, password)
        }
    }
}
package com.jefisu.chatapp.features_profile.domain.use_cases

import com.jefisu.chatapp.core.data.model.User
import com.jefisu.chatapp.core.data.repository.SharedRepository
import com.jefisu.chatapp.core.util.Resource
import com.jefisu.chatapp.core.util.requestCatch

class GetChangesUser(
    private val repository: SharedRepository
) {
    suspend operator fun invoke(username: String): Resource<User> {
        return requestCatch {
            repository.getUser(username)
        }
    }
}
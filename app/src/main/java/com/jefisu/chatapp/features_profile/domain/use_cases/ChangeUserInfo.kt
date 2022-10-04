package com.jefisu.chatapp.features_profile.domain.use_cases

import com.jefisu.chatapp.core.data.model.User
import com.jefisu.chatapp.core.util.Resource
import com.jefisu.chatapp.core.util.requestCatch
import com.jefisu.chatapp.features_profile.domain.repository.ProfileRepository

class ChangeUserInfo(
    private val repository: ProfileRepository,
) {
    suspend operator fun invoke(user: User): Resource<String> {
        return requestCatch {
            repository.changeUserInfo(user)
        }
    }
}
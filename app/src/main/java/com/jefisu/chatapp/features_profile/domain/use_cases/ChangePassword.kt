package com.jefisu.chatapp.features_profile.domain.use_cases

import com.jefisu.chatapp.core.util.Resource
import com.jefisu.chatapp.core.util.requestCatch
import com.jefisu.chatapp.features_profile.domain.model.Password
import com.jefisu.chatapp.features_profile.domain.repository.ProfileRepository

class ChangePassword(
    private val repository: ProfileRepository,
) {
    suspend operator fun invoke(
        request: Password
    ): Resource<String> {
        return requestCatch {
            repository.changePassword(request)
        }
    }
}
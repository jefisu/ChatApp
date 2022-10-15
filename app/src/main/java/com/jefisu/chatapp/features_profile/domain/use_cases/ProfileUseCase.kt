package com.jefisu.chatapp.features_profile.domain.use_cases

data class ProfileUseCase(
    val changeAvatar: ChangeAvatar,
    val changeUserInfo: ChangeUserInfo,
    val changePassword: ChangePassword
)
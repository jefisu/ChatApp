package com.jefisu.chatapp.features_profile.data.mapper

import com.jefisu.chatapp.features_profile.data.dto.PasswordDto
import com.jefisu.chatapp.features_profile.domain.model.Password

fun Password.toPasswordDto(): PasswordDto {
    return PasswordDto(
        username = username,
        oldPassword = oldPassword,
        newPassword = newPassword
    )
}
package com.jefisu.chatapp.core.data.mapper

import com.jefisu.chatapp.core.data.dto.UserDto
import com.jefisu.chatapp.core.data.model.User

fun UserDto.toUser(): User {
    return User(
        name = name,
        username = username,
        email = email,
        avatarUrl = avatarUrl,
        id = id
    )
}

fun User.toUserDto(): UserDto {
    return UserDto(
        name = name,
        username = username,
        email = email,
        avatarUrl = avatarUrl,
        id = id
    )
}
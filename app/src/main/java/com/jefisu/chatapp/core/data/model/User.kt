package com.jefisu.chatapp.core.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class User(
    val name: String?,
    val username: String,
    val email: String,
    val avatarUrl: String?,
    val id: String
) : Parcelable
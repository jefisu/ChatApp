package com.jefisu.chatapp.features_profile.presentation.edit_password

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EdiPasswordNavArg(
    val username: String
) : Parcelable

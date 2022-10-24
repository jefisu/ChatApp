package com.jefisu.chatapp.core.validate

import android.util.Patterns
import com.jefisu.chatapp.R
import com.jefisu.chatapp.core.util.UiText

object ValidateUtil {
    fun validateEmail(value: String): ValidationResult {
        if (value.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.the_field_cant_be_blank, "email")
            )
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.thats_not_a_valid_email)
            )
        }
        return ValidationResult(
            successful = true
        )
    }

    fun validatePassword(value: String, repeatPassword: String? = null): ValidationResult {
        if (value.length < 8) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.password_consist_least_8_characters)
            )
        }
        val containsLettersAndDigits = value.any { it.isDigit() } &&
            value.any { it.isLetter() }
        if (!containsLettersAndDigits) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(
                    R.string.password_needs_contain_letter_and_digit
                )
            )
        }
        if (repeatPassword != null && value != repeatPassword) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(
                    R.string.the_confirmation_password_must
                )
            )
        }

        return ValidationResult(
            successful = true
        )
    }

    fun validateUsername(value: String): ValidationResult {
        if (value.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.the_field_cant_be_blank, "username")
            )
        }
        if (value.contains(" ")) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(
                    R.string.username_cant_contain_spaces_and_special_characters
                )
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}
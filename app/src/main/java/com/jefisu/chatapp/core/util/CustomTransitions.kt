package com.jefisu.chatapp.core.util

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.navigation.NavBackStackEntry
import com.jefisu.chatapp.appDestination
import com.jefisu.chatapp.destinations.ChatScreenDestination
import com.jefisu.chatapp.destinations.EditPasswordScreenDestination
import com.jefisu.chatapp.destinations.EditProfileScreenDestination
import com.jefisu.chatapp.destinations.HomeScreenDestination
import com.jefisu.chatapp.destinations.LoginScreenDestination
import com.jefisu.chatapp.destinations.ProfileScreenDestination
import com.jefisu.chatapp.destinations.RegistrationScreenDestination
import com.ramcosta.composedestinations.spec.DestinationStyle

@OptIn(ExperimentalAnimationApi::class)
object CustomTransitions : DestinationStyle.Animated {

    private val defaultEnterTransition = slideInHorizontally { 300 } + fadeIn(tween(500))
    private val defaultPopEnterTransition = slideInHorizontally { -300 } + fadeIn(tween(500))

    override fun AnimatedContentScope<NavBackStackEntry>.enterTransition(): EnterTransition? {
        return when (targetState.appDestination()) {
            LoginScreenDestination -> {
                slideInVertically { 300 } + fadeIn(tween(700))
            }
            HomeScreenDestination -> {
                scaleIn(tween(300)) + fadeIn(tween(700))
            }
            RegistrationScreenDestination -> defaultEnterTransition
            ChatScreenDestination -> defaultEnterTransition
            ProfileScreenDestination -> defaultEnterTransition
            EditProfileScreenDestination -> defaultEnterTransition
            EditPasswordScreenDestination -> defaultEnterTransition
            else -> null
        }
    }

    override fun AnimatedContentScope<NavBackStackEntry>.exitTransition(): ExitTransition? {
        return null
    }

    override fun AnimatedContentScope<NavBackStackEntry>.popEnterTransition(): EnterTransition? {
        return when (targetState.appDestination()) {
            LoginScreenDestination -> defaultPopEnterTransition
            HomeScreenDestination -> defaultPopEnterTransition
            ProfileScreenDestination -> defaultPopEnterTransition
            else -> null
        }
    }

    override fun AnimatedContentScope<NavBackStackEntry>.popExitTransition(): ExitTransition? {
        return null
    }
}
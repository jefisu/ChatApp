package com.jefisu.chatapp.core.util

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry
import com.jefisu.chatapp.appDestination
import com.jefisu.chatapp.destinations.*
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
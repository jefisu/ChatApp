package com.jefisu.chatapp.core.util

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry
import com.jefisu.chatapp.appDestination
import com.jefisu.chatapp.destinations.HomeScreenDestination
import com.jefisu.chatapp.destinations.LoginScreenDestination
import com.jefisu.chatapp.destinations.ProfileScreenDestination
import com.ramcosta.composedestinations.spec.DestinationStyle

@OptIn(ExperimentalAnimationApi::class)
object CustomTransitions : DestinationStyle.Animated {

    override fun AnimatedContentScope<NavBackStackEntry>.enterTransition(): EnterTransition? {
        return when (targetState.appDestination()) {
            LoginScreenDestination -> {
                slideInVertically { 300 } + fadeIn(tween(900))
            }
            HomeScreenDestination -> {
                scaleIn(tween(300)) + fadeIn(tween(900))
            }
            else -> slideInHorizontally { it - 900 } + fadeIn(tween(150))
        }
    }

    override fun AnimatedContentScope<NavBackStackEntry>.exitTransition(): ExitTransition? {
        return when (targetState.appDestination()) {
            in listOf(LoginScreenDestination, HomeScreenDestination) -> null
            else -> slideOutHorizontally { -it + 900 } + fadeOut(tween(durationMillis = 150))
        }
    }

    override fun AnimatedContentScope<NavBackStackEntry>.popEnterTransition(): EnterTransition? {
        return when (targetState.appDestination()) {
            in listOf(
                LoginScreenDestination,
                HomeScreenDestination,
                ProfileScreenDestination
            ) -> slideInHorizontally { -it + 900 } + fadeIn(tween(durationMillis = 150))
            else -> null
        }
    }

    override fun AnimatedContentScope<NavBackStackEntry>.popExitTransition(): ExitTransition? {
        return when (targetState.appDestination()) {
            in listOf(
                LoginScreenDestination,
                HomeScreenDestination,
                ProfileScreenDestination
            ) -> slideOutHorizontally { it - 900 } + fadeOut(tween(durationMillis = 150))
            else -> null
        }
    }
}
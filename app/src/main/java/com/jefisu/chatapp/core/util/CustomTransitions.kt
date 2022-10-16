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
                slideInVertically { 300 } + fadeIn(tween(700))
            }
            HomeScreenDestination -> {
                scaleIn(tween(300)) + fadeIn(tween(700))
            }
            else -> fadeIn() + slideInHorizontally { it / 4 }
        }
    }

    override fun AnimatedContentScope<NavBackStackEntry>.exitTransition(): ExitTransition? {
        return when (targetState.appDestination()) {
            in listOf(LoginScreenDestination, HomeScreenDestination) -> null
            else -> fadeOut() + slideOutHorizontally { -it / 4 }
        }
    }

    override fun AnimatedContentScope<NavBackStackEntry>.popEnterTransition(): EnterTransition? {
        return when (targetState.appDestination()) {
            in listOf(
                LoginScreenDestination,
                HomeScreenDestination,
                ProfileScreenDestination
            ) -> fadeIn() + slideInHorizontally { -it / 4 }
            else -> null
        }
    }

    override fun AnimatedContentScope<NavBackStackEntry>.popExitTransition(): ExitTransition? {
        return when (targetState.appDestination()) {
            in listOf(
                LoginScreenDestination,
                HomeScreenDestination,
                ProfileScreenDestination
            ) -> fadeOut() + slideOutHorizontally { it / 4 }
            else -> null
        }
    }
}
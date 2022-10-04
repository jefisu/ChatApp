package com.jefisu.chatapp.core.di

import android.content.SharedPreferences
import com.jefisu.chatapp.features_auth.data.repository.AuthRepositoryImpl
import com.jefisu.chatapp.features_auth.domain.repository.AuthRepository
import com.jefisu.chatapp.features_auth.domain.use_cases.AuthUseCases
import com.jefisu.chatapp.features_auth.domain.use_cases.SignIn
import com.jefisu.chatapp.features_auth.domain.use_cases.SignUp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.ktor.client.*

@Module
@InstallIn(ViewModelComponent::class)
object AuthModule {


    @Provides
    @ViewModelScoped
    fun provideAuthUseCases(
        repository: AuthRepository
    ) = AuthUseCases(
        signIn = SignIn(repository),
        signUp = SignUp(repository)
    )

    @Provides
    @ViewModelScoped
    fun provideAuthRepository(client: HttpClient, prefs: SharedPreferences): AuthRepository {
        return AuthRepositoryImpl(client, prefs)
    }
}
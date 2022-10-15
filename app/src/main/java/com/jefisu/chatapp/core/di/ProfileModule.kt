package com.jefisu.chatapp.core.di

import com.jefisu.chatapp.core.data.repository.SharedRepository
import com.jefisu.chatapp.features_profile.data.repository.ProfileRepositoryImpl
import com.jefisu.chatapp.features_profile.domain.repository.ProfileRepository
import com.jefisu.chatapp.features_profile.domain.use_cases.ChangeAvatar
import com.jefisu.chatapp.features_profile.domain.use_cases.ChangePassword
import com.jefisu.chatapp.features_profile.domain.use_cases.ChangeUserInfo
import com.jefisu.chatapp.features_profile.domain.use_cases.ProfileUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.ktor.client.HttpClient

@Module
@InstallIn(ViewModelComponent::class)
object ProfileModule {

    @Provides
    @ViewModelScoped
    fun provideProfileRepository(client: HttpClient): ProfileRepository {
        return ProfileRepositoryImpl(client)
    }

    @Provides
    @ViewModelScoped
    fun provideProfileUseCase(
        repository: ProfileRepository,
        sharedRepository: SharedRepository
    ): ProfileUseCase {
        return ProfileUseCase(
            changeAvatar = ChangeAvatar(repository, sharedRepository),
            changeUserInfo = ChangeUserInfo(repository, sharedRepository),
            changePassword = ChangePassword(repository)
        )
    }
}
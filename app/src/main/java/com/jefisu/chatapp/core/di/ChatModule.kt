package com.jefisu.chatapp.core.di

import com.jefisu.chatapp.core.data.repository.SharedRepository
import com.jefisu.chatapp.features_chat.data.repository.ChatRepositoryImpl
import com.jefisu.chatapp.features_chat.data.services.ChatSocketServiceImpl
import com.jefisu.chatapp.features_chat.domain.repository.ChatRepository
import com.jefisu.chatapp.features_chat.domain.services.ChatSocketService
import com.jefisu.chatapp.features_chat.domain.use_cases.ChatUseCases
import com.jefisu.chatapp.features_chat.domain.use_cases.ConnectToChat
import com.jefisu.chatapp.features_chat.domain.use_cases.DeleteChat
import com.jefisu.chatapp.features_chat.domain.use_cases.ExitChat
import com.jefisu.chatapp.features_chat.domain.use_cases.GetAllUsers
import com.jefisu.chatapp.features_chat.domain.use_cases.GetChat
import com.jefisu.chatapp.features_chat.domain.use_cases.GetChatsByUser
import com.jefisu.chatapp.features_chat.domain.use_cases.GetUser
import com.jefisu.chatapp.features_chat.domain.use_cases.ObserveMessages
import com.jefisu.chatapp.features_chat.domain.use_cases.SendMessage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.ktor.client.HttpClient

@Module
@InstallIn(ViewModelComponent::class)
object ChatModule {

    @Provides
    @ViewModelScoped
    fun provideChatSocketService(client: HttpClient): ChatSocketService {
        return ChatSocketServiceImpl(client)
    }

    @Provides
    @ViewModelScoped
    fun provideChatRepository(client: HttpClient): ChatRepository {
        return ChatRepositoryImpl(client)
    }

    @Provides
    @ViewModelScoped
    fun provideChatUseCases(
        repository: ChatRepository,
        sharedRepository: SharedRepository,
        service: ChatSocketService
    ): ChatUseCases {
        return ChatUseCases(
            getChatsByUser = GetChatsByUser(repository),
            getAllUsers = GetAllUsers(repository),
            getUser = GetUser(sharedRepository),
            connectToChat = ConnectToChat(service),
            exitChat = ExitChat(service),
            getChat = GetChat(repository),
            observeMessages = ObserveMessages(service),
            sendMessage = SendMessage(service),
            deleteChat = DeleteChat(repository)
        )
    }
}
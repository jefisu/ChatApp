package com.jefisu.chatapp.features_chat.presentation.home

import android.content.SharedPreferences
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.chatapp.core.data.model.User
import com.jefisu.chatapp.core.util.Resource
import com.jefisu.chatapp.features_chat.core.util.DateUtil
import com.jefisu.chatapp.features_chat.domain.use_cases.ChatUseCases
import com.jefisu.chatapp.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val chatUseCases: ChatUseCases,
    private val savedStateHandle: SavedStateHandle,
    private val prefs: SharedPreferences
) : ViewModel() {

    private val user = savedStateHandle.navArgs<User>()

    private val chatsFlow = chatUseCases.getChatsByUser(user)
    private val usersFlow = chatUseCases.getAllUsers(user.username)
    private val userFlow = savedStateHandle.getStateFlow("user", user)

    val state = combine(chatsFlow, usersFlow, userFlow) { chats, users, curUser ->
        val filteredUsers = users.filterIndexed { index, _ -> index < 10 }
        HomeState(
            users = filteredUsers,
            ownerUser = curUser,
            chats = chats.map { chat ->
                val user = chat.users.first { it != curUser }
                val lastMessage = chat.messages.firstOrNull()
                ChatPreview(
                    id = chat.id,
                    recipientUser = user,
                    lastMessage = lastMessage,
                    ownerSentLastMessage = curUser == user,
                    timeLastMessage = DateUtil.getLastMessageTime(lastMessage?.timestamp ?: 0L)
                )
            }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    fun getUser() {
        val username = prefs.getString("username", null) ?: user.username
        viewModelScope.launch {
            val result = chatUseCases.getUser(username)
            when (result) {
                is Resource.Success -> {
                    savedStateHandle["user"] = result.data
                }
                is Resource.Error -> Unit
            }
        }
    }
}
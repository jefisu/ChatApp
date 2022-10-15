package com.jefisu.chatapp.features_chat.presentation.home

import android.content.SharedPreferences
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.chatapp.core.data.model.User
import com.jefisu.chatapp.features_chat.core.util.DateUtil
import com.jefisu.chatapp.features_chat.domain.model.Chat
import com.jefisu.chatapp.features_chat.domain.model.Message
import com.jefisu.chatapp.features_chat.domain.use_cases.ChatUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val chatUseCases: ChatUseCases,
    private val savedStateHandle: SavedStateHandle,
    private val prefs: SharedPreferences
) : ViewModel() {

    private val chats = savedStateHandle.getStateFlow("chats", emptyList<Chat>())
    private val users = savedStateHandle.getStateFlow("users", emptyList<User>())
    private val ownerUser = savedStateHandle.getStateFlow<User?>("user", null)
    private val searchQuery = savedStateHandle.getStateFlow("searchQuery", "")

    val state =
        combine(chats, users, ownerUser, searchQuery) { chats, users, curUser, searchQuery ->
            val filteredUsers = users.filterIndexed { index, _ -> index < 10 }
            val curChats = chats.map { chat ->
                val user = chat.users.first { it != curUser }
                val lastMessage = chat.messages.firstOrNull()
                ChatPreview(
                    id = chat.id,
                    recipientUser = user,
                    lastMessage = lastMessage,
                    ownerSentLastMessage = curUser == user,
                    timeLastMessage = DateUtil.getLastMessageTime(lastMessage?.timestamp ?: 0L),
                    messages = chat.messages.toMutableList() as ArrayList<Message>
                )
            }.filter { chat ->
                chat.lastMessage?.text?.contains(searchQuery, true) ?: false
                        || chat.recipientUser.name?.contains(searchQuery, true) ?: false
                        || chat.recipientUser.username.contains(searchQuery, true)
            }
            HomeState(
                searchQuery = searchQuery,
                users = filteredUsers,
                ownerUser = curUser,
                chats = curChats
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    fun loadChatsUsers() {
        viewModelScope.launch {
            ownerUser.value?.let {
                launch {
                    savedStateHandle["chats"] = chatUseCases.getChatsByUser(it)
                }
                launch {
                    savedStateHandle["users"] = chatUseCases.getAllUsers(it.username)
                }
            }
        }
    }

    fun getUser() {
        viewModelScope.launch {
            val parsedUser = prefs.getString("user", null)
            parsedUser?.let { json ->
                val user = Json.decodeFromString<User>(json)
                savedStateHandle["user"] = user
            }
        }
    }

    fun searchChats(query: String) {
        savedStateHandle["searchQuery"] = query
    }
}
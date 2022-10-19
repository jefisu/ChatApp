package com.jefisu.chatapp.features_chat.presentation.home

import android.content.SharedPreferences
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.chatapp.core.data.model.User
import com.jefisu.chatapp.core.util.Resource
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
    private val selectedChats = savedStateHandle.getStateFlow("selectedChats", emptyList<ChatPreview>())

    val state = combine(
        chats,
        users,
        ownerUser,
        searchQuery,
        selectedChats
    ) { chats, users, curUser, searchQuery, selectedChats ->
        val filteredUsers = users.filterIndexed { index, _ -> index < 10 }
        val curChats = chats.map { chat ->
            val userId = chat.userIds.find { it != curUser?.id }
            val user = users.find { it.id == userId }
            val lastMessage = chat.messages.firstOrNull()
            ChatPreview(
                id = chat.id,
                recipientUser = user,
                lastMessage = lastMessage,
                ownerSentLastMessage = curUser?.id == user?.id,
                timeLastMessage = DateUtil.getLastMessageTime(lastMessage?.timestamp ?: 0L),
                messages = chat.messages.toMutableList() as ArrayList<Message>
            )
        }.filter { chat ->
            chat.lastMessage?.text?.contains(searchQuery, true) ?: false
                    || chat.recipientUser?.name?.contains(searchQuery, true) ?: false
                    || chat.recipientUser?.username?.contains(searchQuery, true) ?: false
        }
        HomeState(
            searchQuery = searchQuery,
            users = filteredUsers,
            ownerUser = curUser,
            chats = curChats,
            selectedChats = selectedChats
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    init {
        getUsers()
        loadChatsUsers()
    }

    private fun loadChatsUsers() {
        viewModelScope.launch {
            ownerUser.value?.let {
                savedStateHandle["chats"] = chatUseCases.getChatsByUser(it.id)
            }
        }
    }

    fun getUsers() {
        viewModelScope.launch {
            val parsedUser = prefs.getString("user", null)
            parsedUser?.let { json ->
                val ownerUser = Json.decodeFromString<User>(json)
                savedStateHandle["user"] = ownerUser
                savedStateHandle["users"] = chatUseCases.getAllUsers(ownerUser.username)
            }
        }
    }

    fun searchChats(query: String) {
        savedStateHandle["searchQuery"] = query
    }

    fun updateMessageByCurrentChat(chatId: String?, messages: List<Message>) {
        if (chatId == null) {
            loadChatsUsers()
            return
        }
        savedStateHandle["chats"] = chats.value.toMutableList().apply {
            replaceAll {
                if (it.id == chatId) {
                    it.copy(messages = messages)
                } else it
            }
        }
    }

    fun clearSelection() {
        savedStateHandle["selectedChats"] = emptyList<ChatPreview>()
    }

    fun selectChat(chat: ChatPreview) {
        if (selectedChats.value.contains(chat)) {
            savedStateHandle["selectedChats"] = selectedChats.value - chat
            return
        }
        savedStateHandle["selectedChats"] = selectedChats.value + chat
    }

    fun selectAll() {
        if (selectedChats.value.size == chats.value.size) {
            clearSelection()
            return
        }
        clearSelection()
        savedStateHandle["selectedChats"] = state.value?.chats
    }

    fun deleteChat() {
        viewModelScope.launch {
            if (selectedChats.value.isNotEmpty()) {
                selectedChats.value.forEach { chat ->
                    val result = chatUseCases.deleteChat(chat.id)
                    when (result) {
                        is Resource.Success -> {
                            savedStateHandle["chats"] = chats.value.toMutableList().apply {
                               removeIf { chat.id == it.id }
                            }
                            savedStateHandle["selectedChats"] = selectedChats.value.toMutableList().apply {
                                removeIf { it.id == chat.id }
                            }
                        }
                        is Resource.Error -> Unit
                    }
                }
            }
        }
    }
}
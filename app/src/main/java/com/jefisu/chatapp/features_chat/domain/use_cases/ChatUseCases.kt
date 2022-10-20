package com.jefisu.chatapp.features_chat.domain.use_cases

data class ChatUseCases(
    val getChatsByUser: GetChatsByUser,
    val getAllUsers: GetAllUsers,
    val getUser: GetUser,
    val connectToChat: ConnectToChat,
    val exitChat: ExitChat,
    val getChat: GetChat,
    val observeMessages: ObserveMessages,
    val sendMessage: SendMessage,
    val deleteChat: DeleteChat,
    val  clearChat: ClearChat,
    val deleteMessage: DeleteMessage
)
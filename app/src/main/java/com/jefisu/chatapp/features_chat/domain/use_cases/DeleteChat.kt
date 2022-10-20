package com.jefisu.chatapp.features_chat.domain.use_cases

import com.jefisu.chatapp.core.util.Resource
import com.jefisu.chatapp.core.util.requestCatch
import com.jefisu.chatapp.features_chat.data.dto.DeleteResource
import com.jefisu.chatapp.features_chat.domain.repository.ChatRepository

class DeleteChat(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(deleteResource: DeleteResource): Resource<String> {
        return requestCatch {
            repository.deleteChat(deleteResource)
        }
    }
}
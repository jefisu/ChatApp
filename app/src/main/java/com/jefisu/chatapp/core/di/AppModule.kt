package com.jefisu.chatapp.core.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.jefisu.chatapp.core.data.repository.SharedRepository
import com.jefisu.chatapp.core.data.repository.SharedRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient() = HttpClient(CIO) {
        install(WebSockets)
        install(Logging)
        install(ContentNegotiation) { json() }
        defaultRequest {
            contentType(ContentType.Application.Json)
        }
        expectSuccess = true
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(app: Application): SharedPreferences {
        return app.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideSharedRepository(client: HttpClient, prefs: SharedPreferences): SharedRepository {
        return SharedRepositoryImpl(client, prefs)
    }
}
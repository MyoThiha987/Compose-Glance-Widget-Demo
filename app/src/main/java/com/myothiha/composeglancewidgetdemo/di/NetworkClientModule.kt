package com.myothiha.composeglancewidgetdemo.di

import android.content.Context
import android.util.Log
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.myothiha.composeglancewidgetdemo.data.WeatherDto
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.KotlinxSerializationConverter
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import javax.inject.Singleton

/**
 * @Author Liam
 * Created at 03/Sep/2024
 */

@Module
@InstallIn(SingletonComponent::class)
object NetworkServiceModule {

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideHttpClient(
        @ApplicationContext context: Context
    ): HttpClient {

        val converter = KotlinxSerializationConverter(Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            explicitNulls = false
            coerceInputValues = true

        })

        // Create the Interceptor
        val chuckerInterceptor =
            ChuckerInterceptor.Builder(context).collector(ChuckerCollector(context))
                .maxContentLength(250000L).build()

        val okhttpEngine = OkHttp.create {
            addInterceptor(chuckerInterceptor)
        }
        return HttpClient(okhttpEngine) {
            expectSuccess = true

            install(ContentNegotiation) {
                register(
                    ContentType.Application.Json, converter
                )
            }

            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.BODY
            }


            install(ResponseObserver) {
                onResponse { response ->
                    Log.d("HTTP status:", "${response.body<WeatherDto>()}")
                }
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
            }
        }
    }
}
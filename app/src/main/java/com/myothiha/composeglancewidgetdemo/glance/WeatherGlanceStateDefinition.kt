package com.myothiha.composeglancewidgetdemo.glance

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.datastore.dataStoreFile
import androidx.glance.state.GlanceStateDefinition
import com.myothiha.composeglancewidgetdemo.domain.WeatherUIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default.decodeFromString
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/**
 * @Author Liam
 * Created at 03/Sep/2024
 */
private val DS_FILENAME = "weather"
private val Context.datastore by dataStore(DS_FILENAME, WeatherStateSerializer)

object WeatherGlanceStateDefinition : GlanceStateDefinition<WeatherUIState> {
    override suspend fun getDataStore(
        context: Context,
        fileKey: String
    ): DataStore<WeatherUIState> {
        return context.datastore
    }

    override fun getLocation(context: Context, fileKey: String): File {
        return context.dataStoreFile(DS_FILENAME)
    }
}

object WeatherStateSerializer : Serializer<WeatherUIState> {
    override val defaultValue: WeatherUIState
        get() = WeatherUIState.Error("No Data Available")

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override suspend fun readFrom(input: InputStream): WeatherUIState {
        return try {
            decodeFromString<WeatherUIState>(input.readAllBytes().decodeToString())
        } catch (exception: IOException) {
            WeatherUIState.Error("Coroutine Error")
        }
    }

    override suspend fun writeTo(t: WeatherUIState, output: OutputStream) {
        output.use {
            it.write(Json.encodeToString(t).encodeToByteArray())
        }
    }
}
package com.myothiha.composeglancewidgetdemo.glance

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.myothiha.composeglancewidgetdemo.domain.WeatherRepository
import com.myothiha.composeglancewidgetdemo.domain.WeatherUIState
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.Duration
import kotlin.random.Random

/**
 * @Author Liam
 * Created at 03/Sep/2024
 */

@HiltWorker
class WeatherDataFetchingWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val repository: WeatherRepository

) : CoroutineWorker(appContext = context, params = workerParameters) {


    companion object {
        private val uniqueWorkName = WeatherDataFetchingWorker::class.java.simpleName

        @RequiresApi(Build.VERSION_CODES.O)
        fun enqueue(context: Context, force: Boolean = false) {
            val manager = WorkManager.getInstance(context)
            val requestBuilder =
                PeriodicWorkRequestBuilder<WeatherDataFetchingWorker>(Duration.ofMinutes(30)).build()
            val workPolicy = if (force) {
                ExistingPeriodicWorkPolicy.REPLACE
            } else {
                ExistingPeriodicWorkPolicy.KEEP
            }

            manager.enqueueUniquePeriodicWork(
                "$uniqueWorkName-workaround",
                workPolicy,
                requestBuilder
            )
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelAllWork()
        }
    }

    override suspend fun doWork(): Result {
        val cities = listOf("Bangkok", "Myanmar", "Singapore", "Hiroshima", "Tokyo")
        return runCatching {
            //Loading
            updateImageWidget(WeatherUIState.Loading)
            // Success
            val data = repository.fetchCurrentCityWeather(cities[Random.nextInt(5)])

            updateImageWidget(WeatherUIState.Success(data))

            Result.success()
        }.getOrElse {
            Result.failure()
        }
    }

    private suspend fun updateImageWidget(newState: WeatherUIState) {
        val manager = GlanceAppWidgetManager(context = context)
        val glanceIds = manager.getGlanceIds(AppWidget::class.java)
        glanceIds.forEach { glanceId ->
            updateAppWidgetState(
                context,
                definition = WeatherGlanceStateDefinition,
                glanceId = glanceId,
                updateState = { newState }
            )
        }
        AppWidget().updateAll(context)
    }
}
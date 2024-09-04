package com.myothiha.composeglancewidgetdemo.glance

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

/**
 * @Author Liam
 * Created at 02/Sep/2024
 */

class AppWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget get() = AppWidget()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        context?.let {
            WeatherDataFetchingWorker.enqueue(context=it,force = true)
        }
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        context?.let {
            WeatherDataFetchingWorker.cancel(context)
        }
    }
}
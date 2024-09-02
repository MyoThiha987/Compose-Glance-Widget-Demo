package com.myothiha.composeglancewidgetdemo.glance

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

/**
 * @Author Liam
 * Created at 02/Sep/2024
 */

class AppWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget get() = AppWidget()
}
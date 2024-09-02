package com.myothiha.composeglancewidgetdemo.glance

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import androidx.glance.text.Text
import com.myothiha.composeglancewidgetdemo.R

/**
 * @Author Liam
 * Created at 02/Sep/2024
 */

class AppWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                MyContent()
            }
        }
    }
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview
@Composable
fun MyContent() {
    Column(
        modifier = GlanceModifier.fillMaxSize()
            .background(GlanceTheme.colors.background)
    ) {
        Text(text = "Hello")
        Image(
            modifier = GlanceModifier.padding(16.dp).size(80.dp),
            provider = ImageProvider(R.drawable.ic_cloud),
            contentDescription = null
        )
    }
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 300, heightDp = 200)
@Composable
fun MyContentPreview() {
    MyContent()
}

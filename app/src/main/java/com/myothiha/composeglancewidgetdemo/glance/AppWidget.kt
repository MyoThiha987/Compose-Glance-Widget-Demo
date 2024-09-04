package com.myothiha.composeglancewidgetdemo.glance

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.clickable
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.layout.wrapContentHeight
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.myothiha.composeglancewidgetdemo.R
import com.myothiha.composeglancewidgetdemo.domain.WeatherUIState
import kotlinx.coroutines.launch

/**
 * @Author Liam
 * Created at 02/Sep/2024
 */

class AppWidget : GlanceAppWidget() {
    override val sizeMode: SizeMode get() = SizeMode.Exact

    override val stateDefinition = WeatherGlanceStateDefinition

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                val state = currentState<WeatherUIState>()
                MyContent(state = state)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalGlancePreviewApi::class)
@Preview
@Composable
fun MyContent(state: WeatherUIState) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    when (state) {
        is WeatherUIState.Loading -> {
            Box(
                modifier = GlanceModifier.fillMaxSize().background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = GlanceModifier,
                    color = ColorProvider(Color.Black)
                )
            }
        }

        is WeatherUIState.Success -> {
            Box(
                modifier = GlanceModifier.fillMaxSize().wrapContentHeight()
                    .clickable {
                        scope.launch {
                            WeatherDataFetchingWorker.enqueue(context = context, force = true)
                        }
                    }
            ) {

                Column(
                    modifier = GlanceModifier
                        .padding(all = 16.dp)
                        .background(Color.White),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = state.data.locationName,
                        modifier = GlanceModifier,
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    )

                    Spacer(modifier = GlanceModifier.height(12.dp))

                    Text(
                        text = "${state.data.temperature}°C",
                        modifier = GlanceModifier.padding(vertical = 12.dp),
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 22.sp)
                    )

                    Row(
                        modifier = GlanceModifier.fillMaxWidth().wrapContentHeight(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "H:24' L:28'",
                            modifier = GlanceModifier,
                            style = TextStyle(fontWeight = FontWeight.Normal, fontSize = 10.sp)
                        )
                        Spacer(modifier = GlanceModifier.defaultWeight())
                        Text(
                            text = state.data.weatherCondition,
                            modifier = GlanceModifier,
                            style = TextStyle(fontWeight = FontWeight.Normal, fontSize = 10.sp)
                        )
                    }

                    Spacer(modifier = GlanceModifier.height(20.dp))

                    Box(
                        modifier = GlanceModifier
                            .fillMaxWidth()
                            .height(0.1.dp)
                            .background(Color.Black.copy(alpha = 0.2f))
                            .padding(start = 12.dp, end = 12.dp)
                    ) { }

                    Row(modifier = GlanceModifier.fillMaxWidth().padding(top = 20.dp)) {
                        Column(
                            modifier = GlanceModifier.defaultWeight()
                        ) {

                            Row {

                                Image(
                                    provider = ImageProvider(R.drawable.ic_clouds),
                                    modifier = GlanceModifier.size(12.dp),
                                    contentDescription = ""
                                )
                                Text(
                                    text = "Precipitation",
                                    modifier = GlanceModifier.padding(horizontal = 4.dp),
                                    style = TextStyle(
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                            Text(
                                text = "${state.data.preciptation}mm in 24hours",
                                modifier = GlanceModifier.padding(vertical = 4.dp),
                                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 10.sp)
                            )
                        }

                        Box(
                            modifier = GlanceModifier
                                .width(0.1.dp)
                                .height(34.dp)
                                .background(Color.Gray.copy(alpha = 0.2f))
                        ) { }

                        Column(
                            modifier = GlanceModifier.defaultWeight(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(modifier = GlanceModifier) {
                                Image(
                                    provider = ImageProvider(R.drawable.ic_wind),
                                    modifier = GlanceModifier.size(12.dp),
                                    contentDescription = ""
                                )
                                Text(
                                    text = "Wind",
                                    modifier = GlanceModifier.padding(horizontal = 4.dp),
                                    style = TextStyle(
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                            Text(
                                text = "${state.data.windSpeed} miles per hour",
                                modifier = GlanceModifier.padding(
                                    horizontal = 12.dp,
                                    vertical = 4.dp
                                ),
                                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 10.sp)
                            )
                        }

                        Box(
                            modifier = GlanceModifier
                                .width(0.1.dp)
                                .height(34.dp)
                                .background(Color.Gray.copy(alpha = 0.2f))
                        ) { }

                        Column(
                            modifier = GlanceModifier.defaultWeight(),
                            horizontalAlignment = Alignment.End

                        ) {
                            Row(modifier = GlanceModifier) {
                                Image(
                                    provider = ImageProvider(R.drawable.ic_humidity),
                                    modifier = GlanceModifier.size(12.dp),
                                    contentDescription = ""
                                )
                                Text(
                                    text = "Humidity",
                                    modifier = GlanceModifier.padding(horizontal = 4.dp),
                                    style = TextStyle(
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                            Text(
                                text = "${state.data.humidity}% in 24h",
                                modifier = GlanceModifier.padding(vertical = 4.dp),
                                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 10.sp)
                            )
                        }
                    }
                }

                val image = when (state.data.code) {
                    1210, 1213, 1216, 1219, 1222, 1225, 1237 -> R.drawable.ic_snowy
                    1003 -> R.drawable.ic_cloudy
                    1006 -> R.drawable.ic_cloud
                    1063, 1183, 1186, 1189, 1192, 1195, 1198, 1276 -> R.drawable.ic_rainy
                    else -> R.drawable.ic_sunny
                }
                //Cloud

                Row(
                    modifier = GlanceModifier.fillMaxWidth()
                        .padding(vertical = 24.dp, horizontal = 12.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Column(horizontalAlignment = Alignment.End) {
                        Image(
                            modifier = GlanceModifier.size(60.dp),
                            provider = ImageProvider(image),
                            contentDescription = null,
                        )
                    }
                }
            }
        }

        is WeatherUIState.Error -> {
            Box(
                modifier = GlanceModifier.fillMaxSize().background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(text = state.message, style = TextStyle(color = ColorProvider(Color.White)))
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 500)
@Composable
fun MyContentPreview() {
    MyContent(state = WeatherUIState.Loading)
}

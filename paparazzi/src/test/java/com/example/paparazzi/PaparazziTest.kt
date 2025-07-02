package com.example.paparazzi

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.UiMode
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.cash.paparazzi.RenderExtension
import app.cash.paparazzi.detectEnvironment
import com.android.ide.common.rendering.api.SessionParams
import org.junit.Rule
import org.junit.Test
import androidx.compose.ui.unit.dp
import com.example.paparazzisample.ui.theme.PaparazziSampleTheme
import java.util.Locale

class PaparazziTest {

    @get: Rule
    val paparazzi = InternalPaparazziWrapper(screenHeight = 250)

    @Test
    fun test() {
        paparazzi.internalSnapshot() {
            PaparazziSampleTheme {
                BoxWithSomeContent()
            }
        }
    }
}

@Composable
private fun BoxWithSomeContent() {
    Box(
        modifier = Modifier
            .width(100.dp)
            .height(100.dp)
            .background(color = Color.Red)
    )
}

internal fun InternalPaparazziWrapper(
    screenHeight: Int,
    deviceConfig: DeviceConfig = DeviceConfig.PIXEL_2.copy(screenHeight = screenHeight),
    theme: String = "android:ThemeOverlay.Material.Dark",
    maxPercentDifference: Double = 0.1,
    renderExtensions: Set<RenderExtension> = setOf(),
    renderingMode: SessionParams.RenderingMode = SessionParams.RenderingMode.NORMAL
) = Paparazzi(
    deviceConfig = deviceConfig,
    theme = theme,
    maxPercentDifference = maxPercentDifference,
    renderExtensions = renderExtensions,
    renderingMode = renderingMode,
    environment = detectEnvironment(),
)

private const val DefaultFontScale = 1.0f

internal fun Paparazzi.internalSnapshot(
    @UiMode uiMode: Int = UI_MODE_NIGHT_NO,
    fontScale: Float = DefaultFontScale,
    name: String? = null,
    content: @Composable () -> Unit,
) {
    Locale.setDefault(Locale("nb", ""))
    this.context.resources.configuration.apply {
        this.uiMode = uiMode
        this.fontScale = fontScale
    }
    this.snapshot(name = name) {
        content()
    }
}

package com.tunzy.app.ui.theme

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.unit.dp
import com.tunzy.app.service.TunzyState
import com.tunzy.app.service.TunzyStateHolder

@Composable
fun TunzyApp() {
    val state by TunzyStateHolder.state.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        GlowingOrb(state = state)
    }
}

@Composable
fun GlowingOrb(state: TunzyState) {
    val infiniteTransition = rememberInfiniteTransition(label = "orb")

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = when (state) {
                    TunzyState.LISTENING -> 600
                    TunzyState.SPEAKING  -> 400
                    else                 -> 2000
                },
                easing = EaseInOutSine
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = when (state) {
                    TunzyState.LISTENING -> 500
                    TunzyState.SPEAKING  -> 300
                    else                 -> 2500
                },
                easing = EaseInOutSine
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    val coreColor = when (state) {
        TunzyState.IDLE      -> Color(0xFF9B5CFF)
        TunzyState.WAKE      -> Color(0xFF00CFFF)
        TunzyState.LISTENING -> Color(0xFF7B2FFF)
        TunzyState.THINKING  -> Color(0xFF4040FF)
        TunzyState.SPEAKING  -> Color(0xFF00EEFF)
    }

    val outerColor = when (state) {
        TunzyState.IDLE      -> Color(0xFF3A006F)
        TunzyState.WAKE      -> Color(0xFF003C8F)
        TunzyState.LISTENING -> Color(0xFF2A007F)
        TunzyState.THINKING  -> Color(0xFF00007F)
        TunzyState.SPEAKING  -> Color(0xFF003F6F)
    }

    Canvas(modifier = Modifier.size(260.dp)) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val radius = size.minDimension / 2f * pulseScale

        // Outer soft glow
        drawCircle(
            brush = ShaderBrush(RadialGradientShader(
                center = center,
                radius = radius * 1.9f,
                colors = listOf(outerColor.copy(alpha = glowAlpha * 0.3f), Color.Transparent)
            )),
            radius = radius * 1.9f, center = center
        )

        // Mid glow
        drawCircle(
            brush = ShaderBrush(RadialGradientShader(
                center = center,
                radius = radius * 1.3f,
                colors = listOf(
                    coreColor.copy(alpha = glowAlpha * 0.5f),
                    outerColor.copy(alpha = glowAlpha * 0.1f),
                    Color.Transparent
                )
            )),
            radius = radius * 1.3f, center = center
        )

        // Core orb
        drawCircle(
            brush = ShaderBrush(RadialGradientShader(
                center = Offset(center.x - radius * 0.2f, center.y - radius * 0.2f),
                radius = radius,
                colors = listOf(Color.White.copy(alpha = 0.6f), coreColor, outerColor)
            )),
            radius = radius, center = center
        )
    }
}
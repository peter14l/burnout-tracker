package com.burnouttracker.ui.animations

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer

/**
 * Smooth fade-in animation for content
 */
@Composable
fun FadeInAnimation(
    visible: Boolean = true,
    duration: Int = 300,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            animationSpec = tween(durationMillis = duration, easing = FastOutSlowInEasing)
        ) + slideInVertically(
            initialOffsetY = { it / 20 },
            animationSpec = tween(durationMillis = duration, easing = FastOutSlowInEasing)
        ),
        exit = fadeOut(
            animationSpec = tween(durationMillis = duration / 2)
        )
    ) {
        content()
    }
}

/**
 * Scale-in animation for interactive elements
 */
@Composable
fun ScaleInAnimation(
    visible: Boolean = true,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(
            initialScale = 0.9f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ) + fadeIn(),
        exit = scaleOut() + fadeOut()
    ) {
        content()
    }
}

/**
 * Shimmer loading effect
 */
@Composable
fun ShimmerEffect(
    modifier: Modifier = Modifier
) {
    val shimmerColors = listOf(
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer_translate"
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 200f, translateAnim - 200f),
        end = Offset(translateAnim, translateAnim)
    )

    Box(
        modifier = modifier.background(brush)
    )
}

@Composable
private fun Box(modifier: Modifier, content: @Composable () -> Unit = {}) {
    androidx.compose.foundation.layout.Box(modifier = modifier) {
        content()
    }
}

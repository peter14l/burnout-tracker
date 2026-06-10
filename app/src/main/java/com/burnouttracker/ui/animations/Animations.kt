package com.burnouttracker.ui.animations

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.*

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
 * Pulse animation for attention-grabbing elements
 */
@Composable
fun PulseAnimation(
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Box(
        modifier = if (enabled) Modifier.graphicsLayer(scaleX = scale, scaleY = scale) else Modifier
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
        modifier = modifier
            .background(brush)
    )
}

/**
 * Bounce animation for icons/buttons
 */
@Composable
fun BounceAnimation(
    trigger: Boolean = false,
    content: @Composable () -> Unit
) {
    val scale = remember { Animatable(1f) }

    LaunchedEffect(trigger) {
        if (trigger) {
            scale.animateTo(
                targetValue = 0.9f,
                animationSpec = tween(100)
            )
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
    }

    Box(modifier = Modifier.graphicsLayer(scaleX = scale.value, scaleY = scale.value)) {
        content()
    }
}

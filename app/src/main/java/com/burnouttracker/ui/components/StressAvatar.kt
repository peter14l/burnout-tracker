package com.burnouttracker.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.burnouttracker.ui.theme.*

/**
 * 3D-style avatar that changes expression based on stress level
 * Architecture ready for real 3D avatar integration later
 */
@Composable
fun StressAvatar(
    stressLevel: Int,
    modifier: Modifier = Modifier,
    size: AvatarSize = AvatarSize.Large
) {
    val infiniteTransition = rememberInfiniteTransition(label = "avatar_breathe")
    val breatheScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathe"
    )

    val avatarData = getAvatarForStress(stressLevel)

    Box(
        modifier = modifier.size(size.dp),
        contentAlignment = Alignment.Center
    ) {
        // Outer glow
        Box(
            modifier = Modifier
                .size(size.dp * 0.95f)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            avatarData.glowColor.copy(alpha = 0.3f),
                            avatarData.glowColor.copy(alpha = 0.0f)
                        )
                    )
                )
        )

        // Avatar body (3D effect with shadows)
        Box(
            modifier = Modifier
                .size(size.dp * 0.85f * breatheScale)
                .shadow(
                    elevation = 12.dp,
                    shape = CircleShape,
                    ambientColor = avatarData.shadowColor.copy(alpha = 0.3f),
                    spotColor = avatarData.shadowColor.copy(alpha = 0.5f)
                )
                .clip(CircleShape)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            avatarData.gradientTop,
                            avatarData.gradientBottom
                        )
                    )
                )
                .border(
                    width = 3.dp,
                    color = Color.White.copy(alpha = 0.5f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Face emoji
                Text(
                    text = avatarData.emoji,
                    fontSize = (size.dp.value * 0.4f).sp,
                    textAlign = TextAlign.Center
                )

                // Status label
                Text(
                    text = avatarData.label,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.9f),
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

enum class AvatarSize(val dp: Int) {
    Small(64),
    Medium(96),
    Large(140),
    XLarge(180)
}

data class AvatarData(
    val emoji: String,
    val label: String,
    val gradientTop: Color,
    val gradientBottom: Color,
    val glowColor: Color,
    val shadowColor: Color
)

fun getAvatarForStress(level: Int): AvatarData {
    return when {
        level <= 2 -> AvatarData(
            emoji = "😌",
            label = "Zen",
            gradientTop = Color(0xFF81C784),    // Sage green
            gradientBottom = Color(0xFF66BB6A),
            glowColor = Color(0xFF81C784),
            shadowColor = Color(0xFF2E7D32)
        )
        level <= 4 -> AvatarData(
            emoji = "🙂",
            label = "Good",
            gradientTop = Color(0xFFFFD54F),    // Warm yellow
            gradientBottom = Color(0xFFFFCA28),
            glowColor = Color(0xFFFFD54F),
            shadowColor = Color(0xFFF9A825)
        )
        level <= 6 -> AvatarData(
            emoji = "😐",
            label = "Okay",
            gradientTop = Color(0xFFFFAB91),    // Soft coral
            gradientBottom = Color(0xFFFF8A65),
            glowColor = Color(0xFFFFAB91),
            shadowColor = Color(0xFFE64A19)
        )
        level <= 8 -> AvatarData(
            emoji = "😫",
            label = "Stressed",
            gradientTop = Color(0xFFEF9A9A),    // Muted red
            gradientBottom = Color(0xFFEF5350),
            glowColor = Color(0xFFEF9A9A),
            shadowColor = Color(0xFFC62828)
        )
        else -> AvatarData(
            emoji = "😵",
            label = "Overwhelmed",
            gradientTop = Color(0xFFCE93D8),    // Soft purple
            gradientBottom = Color(0xFFBA68C8),
            glowColor = Color(0xFFCE93D8),
            shadowColor = Color(0xFF6A1B9A)
        )
    }
}

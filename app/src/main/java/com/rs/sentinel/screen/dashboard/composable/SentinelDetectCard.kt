package com.rs.sentinel.screen.dashboard.composable

import android.graphics.drawable.Icon
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rs.sentinel.app.R
import com.rs.sentinel.detector.Threat
import com.rs.sentinel.violation.SecurityViolation
import kotlin.reflect.KClass

@Composable
internal fun SentinelDetectCard(
    detectorName: String,
    detectorSeverity: String,
    threats: List<Threat>,
    detected: Set<KClass<out SecurityViolation>>,
    colors: SentinelCardColors = SafeCardColors,
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 12.dp,
                vertical = 8.dp
            )
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(size = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        border = BorderStroke(
            width = 0.5.dp,
            brush = Brush.linearGradient(colors = colors.borderGradient)
        )
    ) {
        Box(
            modifier = Modifier.background(
                Brush.verticalGradient(colors = colors.backgroundGradient)
            )
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = detectorName,
                        style = MaterialTheme.typography.titleSmall,
                        color = colors.textColor,
                        fontWeight = FontWeight.SemiBold
                    )

                    Icon(
                        modifier = Modifier.size(size = 16.dp),
                        imageVector = if (expanded) {
                            Icons.Default.KeyboardArrowUp
                        } else {
                            Icons.Default.KeyboardArrowDown
                        },
                        tint = colors.textColor,
                        contentDescription = null,
                    )
                }

                if (expanded) {
                    threats.forEachIndexed { index, threat ->
                        val threatName = threat.violation::class.simpleName.orEmpty()
                        val severity = threat.violation.severity
                        val isDetected = detected.contains(threat.violation::class)

                        HorizontalDivider(
                            thickness = 0.3.dp,
                            color = colors.textColor.copy(alpha = 0.2f)
                        )

                        SentinelDetectCardItem(
                            title = threatName,
                            value = "${stringResource(id = R.string.severity)}: $severity",
                            isDangerColors = colors == DangerCardColors,
                            isDetected = isDetected,
                            colors = colors
                        )
                    }

                    if (threats.isNotEmpty()) {
                        HorizontalDivider(
                            thickness = 0.3.dp,
                            color = Color.Black.copy(alpha = 0.2f)
                        )

                        SentinelDetectCardItem(
                            modifier = Modifier.background(
                                color = MaterialTheme.colorScheme.background.copy(alpha = 0.25f)
                            ),
                            value = "${stringResource(id = R.string.total_severity)}: $detectorSeverity",
                            colors = colors
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SentinelDetectCardItem(
    modifier: Modifier = Modifier,
    title: String = "",
    value: String,
    isDangerColors: Boolean? = null,
    isDetected: Boolean? = null,
    colors: SentinelCardColors,
    icon: Icon? = null,
) {
    val modifierBackground = when {
        isDetected == true || isDetected == null -> Modifier.background(Color.Transparent)

        else -> if (!isDetected && isDangerColors == true) {
            Modifier.background(Color.DarkGray)
        } else {
            Modifier.background(Color.Transparent)
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(other = modifierBackground)
            .padding(
                vertical = 12.dp,
                horizontal = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (isDetected != null) {
            val iconRes = if (isDetected) {
                R.drawable.ic_circle_error
            } else {
                R.drawable.ic_circle_check
            }

            Icon(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(size = 16.dp),
                painter = painterResource(id = iconRes),
                tint = if (isDetected) colors.iconColor else SafeCardColors.iconColor,
                contentDescription = null,
            )
        }

        Text(
            modifier = Modifier.weight(weight = 1f),
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = colors.textColor,
            textAlign = TextAlign.Start
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                color = colors.textColor.copy(alpha = 0.5f)
            )

            if (icon != null) {
                Spacer(modifier = Modifier.width(width = 8.dp))

                Icon(
                    modifier = Modifier.size(size = 16.dp),
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                )
            }
        }
    }
}
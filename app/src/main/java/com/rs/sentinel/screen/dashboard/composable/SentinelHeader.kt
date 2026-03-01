package com.rs.sentinel.screen.dashboard.composable

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.rs.sentinel.app.R
import com.rs.sentinel.type.RiskLevel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SentinelHeader(
    scrollState: ScrollState,
    packageName: String,
    packageSignature: String,
    riskLevel: RiskLevel,
    severity: Int,
) {
    val collapseRange = 350f

    val collapseFraction =
        (scrollState.value / collapseRange).coerceIn(0f, 1f)

    val scale by animateFloatAsState(
        targetValue = 1f - (collapseFraction * 0.3f),
        label = ""
    )

    val alpha by animateFloatAsState(
        targetValue = 1f - collapseFraction,
        label = ""
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
            }
    ) {
        SentinelPackageCard(
            iconResId = R.mipmap.ic_launcher,
            packageName = packageName,
            packageSignature = packageSignature
        )

        SentinelRiskLevelCard(
            riskLevel = riskLevel,
            severity = severity
        )

        Spacer(modifier = Modifier.height(height = 16.dp))
    }
}
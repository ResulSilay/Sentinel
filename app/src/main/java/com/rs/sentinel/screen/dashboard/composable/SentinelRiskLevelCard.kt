package com.rs.sentinel.screen.dashboard.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rs.sentinel.app.R
import com.rs.sentinel.type.RiskLevel
import com.rs.sentinel.ui.component.RiskLevelBar

@Composable
fun SentinelRiskLevelCard(riskLevel: RiskLevel, severity: Int) {
    val riskDescription = when (riskLevel) {
        RiskLevel.SAFE -> stringResource(id = R.string.risk_safe)
        RiskLevel.LOW -> stringResource(id = R.string.risk_low)
        RiskLevel.MEDIUM -> stringResource(id = R.string.risk_medium)
        RiskLevel.HIGH -> stringResource(id = R.string.risk_high)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(size = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp),
                text = stringResource(id = R.string.risk_level),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                text = riskDescription,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )

            RiskLevelBar(
                level = riskLevel,
                severtiyText = "${stringResource(id = R.string.severity)}: $severity"
            )
        }
    }
}
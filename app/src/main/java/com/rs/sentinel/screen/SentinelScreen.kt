package com.rs.sentinel.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rs.sentinel.Sentinel
import com.rs.sentinel.app.R
import com.rs.sentinel.ext.getAppPackageName
import com.rs.sentinel.ext.getAppSignatureSHA256
import com.rs.sentinel.ext.toByteList
import com.rs.sentinel.model.SecurityReport
import com.rs.sentinel.violation.SecurityViolation

@Composable
internal fun SentinelScreen(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val packageName = remember(context::getAppPackageName)
    val signature = remember(context::getAppSignatureSHA256)

    val sentinel = remember {
        Sentinel.configure(context = context) {
            config {
                this.packageName = packageName.toByteList()
                this.signature = signature.toByteList()
            }

            all()
        }
    }

    val uiState by produceState<SentinelState>(initialValue = SentinelState.Loading) {
        value = runCatching {
            SentinelState.Success(report = sentinel.inspect())
        }.onFailure { exception ->
            SentinelState.Error(throwable = exception)
        }.getOrDefault(SentinelState.Loading)
    }

    when (val state = uiState) {
        is SentinelState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is SentinelState.Success -> {
            val report = state.report

            SentinelContent(
                modifier = modifier,
                report = report,
                packageName = packageName,
                signature = signature
            )
        }

        is SentinelState.Error -> {
            Text("Error: ${state.throwable.message}")
        }
    }
}

@Composable
private fun SentinelContent(
    modifier: Modifier,
    report: SecurityReport,
    packageName: String,
    signature: String,
) {
    val isRisky = report.isCritical()
    val statusColor = if (isRisky) Color(0xFFE53935) else Color(0xFF43A047)
    val statusIcon = if (isRisky) "🚨" else "🛡️"

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StatusCard(
            isRisky = isRisky,
            icon = statusIcon,
            color = statusColor
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        ) {
            SecurityText(
                label = stringResource(id = R.string.package_name),
                value = packageName
            )

            HorizontalDivider()

            SecurityText(
                label = stringResource(id = R.string.package_signature),
                value = signature
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            text = stringResource(id = R.string.security_detail_title),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                val securityItems = listOf(
                    SecurityViolation.Root::class to R.string.check_root,
                    SecurityViolation.Debugger::class to R.string.check_debug_mode,
                    SecurityViolation.Emulator::class to R.string.check_emulator,
                    SecurityViolation.Hook::class to R.string.check_hook,
                    SecurityViolation.Location::class to R.string.check_location,
                )

                Text(
                    text = "${stringResource(id = R.string.score)}: ${report.score} - ${report.riskLevel.name}",
                    style = MaterialTheme.typography.titleMedium,
                )

                securityItems.forEach { (clazz, labelRes) ->
                    SecurityInfo(
                        label = stringResource(labelRes),
                        isFound = report.hasViolationCategory(category = clazz)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusCard(isRisky: Boolean, icon: String, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        border = BorderStroke(1.dp, color.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = stringResource(if (isRisky) R.string.security_status_risky else R.string.security_status_safe),
                    style = MaterialTheme.typography.titleMedium,
                    color = color,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(if (isRisky) R.string.security_desc_risky else R.string.security_desc_safe),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SecurityInfo(label: String, isFound: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(end = 8.dp),
                text = if (isFound) stringResource(R.string.status_danger) else stringResource(R.string.status_clean),
                style = MaterialTheme.typography.labelSmall,
                color = if (isFound) Color.Red else Color(0xFF4CAF50),
            )

            Text(text = if (isFound) "❌" else "✅")
        }
    }
}


@Composable
private fun SecurityText(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
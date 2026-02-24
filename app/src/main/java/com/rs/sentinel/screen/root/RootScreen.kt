package com.rs.sentinel.screen.root

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rs.sentinel.R
import com.rs.sentinel.core.Sentinel

@Composable
internal fun RootScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val root = remember { Sentinel.Root.create(context) }
    val report = remember { root.getReport() }

    val statusColor = if (report.isRooted) Color(0xFFE53935) else Color(0xFF43A047)
    val statusIcon = if (report.isRooted) "🚨" else "🛡️"

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            colors = CardDefaults.cardColors(containerColor = statusColor.copy(alpha = 0.1f)),
            border = androidx.compose.foundation.BorderStroke(
                width = 1.dp,
                color = statusColor.copy(alpha = 0.5f)
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = statusIcon,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.width(width = 12.dp))

                Column {
                    Text(
                        text = stringResource(if (report.isRooted) R.string.security_status_risky else R.string.security_status_safe),
                        style = MaterialTheme.typography.titleMedium,
                        color = statusColor,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(height = 2.dp))

                    Text(
                        text = stringResource(if (report.isRooted) R.string.security_desc_risky else R.string.security_desc_safe),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Text(
            text = stringResource(id = R.string.security_detail_title),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            color = MaterialTheme.colorScheme.secondary
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        ) {
            Column(
                modifier = Modifier.padding(all = 16.dp)
            ) {
                val securityItems = listOf(
                    R.string.check_binary_files to report.foundBinaries,
                    R.string.check_root_apps to report.foundRootApps,
                    R.string.check_system_tags to report.suspiciousProperties,
                    R.string.check_debug_mode to report.isDebuggable,
                    R.string.check_emulator to report.isEmulator,
                    R.string.check_mount_points to report.foundInMounts,
                    R.string.check_runtime_scan to report.foundByRuntime,
                    R.string.check_advanced_paths to report.foundInAdvancedPaths
                )

                securityItems.forEach { (labelRes, isFound) ->
                    SecurityInfo(
                        label = stringResource(labelRes),
                        isFound = isFound
                    )
                }
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

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = if (isFound) stringResource(R.string.status_danger) else stringResource(R.string.status_clean),
                style = MaterialTheme.typography.labelSmall,
                color = if (isFound) Color.Red else Color(0xFF4CAF50),
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = if (isFound) "❌" else "✅")
        }
    }
}
package com.rs.sentinel.screen.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.tooling.preview.Preview
import com.rs.sentinel.Sentinel
import com.rs.sentinel.app.R
import com.rs.sentinel.ext.getAppPackageName
import com.rs.sentinel.ext.getAppSignatureSHA256
import com.rs.sentinel.ext.toByteList
import com.rs.sentinel.screen.dashboard.composable.SentinelHeader
import com.rs.sentinel.screen.dashboard.composable.SentinelDetectors
import com.rs.sentinel.ui.ext.sentinelGradientBackground
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
internal fun SentinelDashboardScreen() {
    val context = LocalContext.current

    val packageName = remember(context::getAppPackageName)
    val packageSignature = remember(context::getAppSignatureSHA256)

    val sentinel = remember {
        Sentinel.configure(context = context) {
            config {
                this.packageName = packageName.toByteList()
                this.packageSignature = packageSignature.toByteList()
                this.threshold = 20
            }

            all()
        }
    }

    val uiState by produceState<SentinelDashboardState>(initialValue = SentinelDashboardState.Loading) {
        value = withContext(context = Dispatchers.IO) {
            runCatching {
                SentinelDashboardState.Success(report = sentinel.inspect())
            }.getOrElse { exception ->
                SentinelDashboardState.Error(throwable = exception)
            }
        }
    }

    Box(
        modifier = Modifier.sentinelGradientBackground(
            riskLevel = (uiState as? SentinelDashboardState.Success)?.report?.riskLevel
        )
    ) {
        when (val state = uiState) {
            is SentinelDashboardState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is SentinelDashboardState.Success -> {
                SentinelDashboardContent(
                    state = state,
                    packageName = packageName,
                    packageSignature = packageSignature
                )
            }

            is SentinelDashboardState.Error -> {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(alignment = Alignment.Center),
                    text = "${stringResource(id = R.string.error)}: ${state.throwable.message}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SentinelDashboardContent(
    state: SentinelDashboardState.Success,
    packageName: String,
    packageSignature: String,
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues = paddingValues)
                .verticalScroll(state = scrollState)
        ) {
            SentinelHeader(
                scrollState = scrollState,
                packageName = packageName,
                packageSignature = packageSignature,
                riskLevel = state.report.riskLevel,
                severity = state.report.severity
            )

            SentinelDetectors(state = state)
        }
    }
}

@Preview
@Composable
private fun SentinelDashboardScreenPreview() {
    SentinelDashboardScreen()
}
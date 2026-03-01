package com.rs.sentinel.screen.dashboard

import com.rs.sentinel.report.SecurityReport

internal sealed interface SentinelDashboardState {

    object Loading : SentinelDashboardState

    data class Success(val report: SecurityReport) : SentinelDashboardState

    data class Error(val throwable: Throwable) : SentinelDashboardState
}
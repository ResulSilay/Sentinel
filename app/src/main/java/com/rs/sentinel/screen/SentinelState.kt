package com.rs.sentinel.screen

import com.rs.sentinel.report.SecurityReport

sealed interface SentinelState {

    object Loading : SentinelState

    data class Success(val report: SecurityReport) : SentinelState

    data class Error(val throwable: Throwable) : SentinelState
}
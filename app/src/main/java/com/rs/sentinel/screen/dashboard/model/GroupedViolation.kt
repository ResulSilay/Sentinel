package com.rs.sentinel.screen.dashboard.model

import androidx.compose.runtime.Immutable
import com.rs.sentinel.detector.Threat
import com.rs.sentinel.violation.SecurityViolation
import kotlin.reflect.KClass

@Immutable
data class GroupedViolation(
    val groupName: String,
    val severity: Int,
    val hasDetected: Boolean,
    val threats: List<Threat>,
    val detectedClasses: Set<KClass<out SecurityViolation>>
)
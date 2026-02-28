package com.rs.kit.location.detector

import android.location.Location
import android.os.Build
import com.rs.sentinel.detector.SecurityDetector
import com.rs.sentinel.detector.Threat
import com.rs.sentinel.violation.SecurityViolation

class MockLocationDetector(
    private val location: Location,
) : SecurityDetector {

    override fun detect(): List<Threat> = buildList {
        if (isLocationMock()) {
            add(
                Threat(
                    violation = SecurityViolation.Location.MockLocationDetected
                )
            )
        }
    }

    private fun isLocationMock(): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            location.isMock
        } else {
            @Suppress("DEPRECATION")
            location.isFromMockProvider
        }
}
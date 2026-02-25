package com.rs.kit.location.detector

import android.location.Location
import android.os.Build
import com.rs.sentinel.detector.SecurityDetector
import com.rs.sentinel.model.Threat
import com.rs.sentinel.type.SecurityType

class MockLocationDetector(
    private val location: Location,
) : SecurityDetector {

    override fun detect(): Threat? = when {
        isLocationMock() -> Threat(
            type = SecurityType.LOCATION,
            description = "Mock location.",
            severity = 30
        )

        else -> null
    }

    private fun isLocationMock(): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            location.isMock
        } else {
            @Suppress("DEPRECATION")
            location.isFromMockProvider
        }
}
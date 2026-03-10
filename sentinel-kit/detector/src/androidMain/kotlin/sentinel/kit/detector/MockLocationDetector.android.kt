package sentinel.kit.detector

import android.location.Location
import android.os.Build
import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat
import sentinel.core.violation.AndroidViolation

class MockLocationDetector(
    private val location: Location,
) : SecurityDetector {

    override fun detect(): List<Threat> = buildList {
        if (isLocationMock()) {
            add(
                element = Threat(
                    violation = AndroidViolation.Location.MockLocationDetected
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
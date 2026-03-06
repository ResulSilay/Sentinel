package sentinel.kit.detector

import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat

actual class DebugDetector actual constructor(
    context: Any?,
) : SecurityDetector {

    actual override fun detect(): List<Threat> = emptyList()
}
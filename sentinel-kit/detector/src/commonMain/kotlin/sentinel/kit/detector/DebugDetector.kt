package sentinel.kit.detector

import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat

expect class DebugDetector(context: Any? = null) : SecurityDetector {

    override fun detect(): List<Threat>
}
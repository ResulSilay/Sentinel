package sentinel.kit.detector

import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat

expect class EmulatorDetector : SecurityDetector {

    override fun detect(): List<Threat>
}
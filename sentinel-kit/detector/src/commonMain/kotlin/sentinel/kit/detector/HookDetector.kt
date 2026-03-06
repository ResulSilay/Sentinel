package sentinel.kit.detector

import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat

expect class HookDetector : SecurityDetector {

    override fun detect(): List<Threat>
}
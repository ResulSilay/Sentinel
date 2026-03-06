package sentinel.kit.detector

import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat

expect class TamperDetector(
    context: Any? = null,
    appId: List<Byte>? = null,
    appSignature: List<Byte>? = null,
) : SecurityDetector {

    override fun detect(): List<Threat>
}
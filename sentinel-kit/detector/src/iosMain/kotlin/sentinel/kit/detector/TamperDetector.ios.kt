package sentinel.kit.detector

import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat

actual class TamperDetector actual constructor(
    context: Any?,
    appId: List<Byte>?,
    appSignature: List<Byte>?,
) : SecurityDetector {

    actual override fun detect(): List<Threat> = emptyList()
}
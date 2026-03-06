package sentinel

import sentinel.core.detector.SecurityDetector
import sentinel.core.identity.Identity
import sentinel.core.report.SecurityReport

class Sentinel internal constructor(
    private val detectors: List<SecurityDetector>,
    val config: Config,
) {

    fun inspect(): SecurityReport {
        val threads = detectors.flatMap { detector -> detector.detect().orEmpty() }

        return SecurityReport(
            threats = threads,
            threshold = config.threshold
        )
    }

    companion object {

        lateinit var Identity: Identity
            internal set
    }
}


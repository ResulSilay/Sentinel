package sentinel

import sentinel.core.detector.SecurityDetector
import sentinel.core.identity.Identity
import sentinel.core.logger.SentinelLogger
import sentinel.core.report.IosSecurityReport
import sentinel.core.report.SecurityReport

actual class Sentinel internal constructor(
    private val detectors: List<SecurityDetector>,
    actual val config: Config,
) {
    actual suspend fun inspect(): SecurityReport = IosSecurityReport(
        threats = detectors.flatMap { detector -> detector.detect().orEmpty() },
        threshold = config.threshold
    ).also { report ->
        if (config.isLoggingEnabled) {
            SentinelLogger.report(report = report)
        }
    }

    companion object {

        lateinit var Identity: Identity
            internal set
    }
}
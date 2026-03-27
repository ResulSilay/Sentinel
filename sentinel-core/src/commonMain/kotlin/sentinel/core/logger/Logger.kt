package sentinel.core.logger

import sentinel.core.report.SecurityReport

expect object SentinelLogger {

    fun info(tag: String = "Sentinel", msg: String)

    fun report(report: SecurityReport)
}
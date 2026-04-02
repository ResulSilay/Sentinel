package sentinel.runtime

import sentinel.core.report.SecurityReport
import sentinel.core.violation.SecurityViolation

expect class Scope() : SecurityScope {

    internal fun dispatch(violation: SecurityViolation, report: SecurityReport)
}
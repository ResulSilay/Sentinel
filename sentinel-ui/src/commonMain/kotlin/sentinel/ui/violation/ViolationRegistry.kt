package sentinel.ui.violation

import sentinel.core.violation.SecurityViolation

expect fun getViolations(): List<SecurityViolation>

expect fun getGroupName(violation: SecurityViolation): String
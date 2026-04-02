package sentinel

import sentinel.core.report.SecurityReport
import sentinel.runtime.SecurityScope

expect class Sentinel {

    val config: Config

    fun runtime(block: SecurityScope.() -> Unit)

    suspend fun inspect(): SecurityReport
}


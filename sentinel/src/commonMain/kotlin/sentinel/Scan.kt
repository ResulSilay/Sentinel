package sentinel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import sentinel.core.guard.SentinelGuard
import sentinel.core.report.SecurityReport

suspend fun Sentinel.scan(
    intervalMs: Long = 30_000,
    block: SentinelGuard.() -> Unit,
) {
    asSecurityFlow(intervalMs = intervalMs).collect { report ->
        SentinelGuard(report = report).block()
    }
}

private fun Sentinel.asSecurityFlow(
    intervalMs: Long = 30_000,
): Flow<SecurityReport> = flow {
    while (true) {
        emit(inspect())
        delay(intervalMs)
    }
}.flowOn(Dispatchers.IO)
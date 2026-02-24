package com.rs.sentinel.runner

import com.rs.sentinel.Sentinel
import com.rs.sentinel.type.SecurityActionType.BLOCK
import com.rs.sentinel.type.SecurityActionType.EXCEPTION
import com.rs.sentinel.type.SecurityActionType.LOG
import com.rs.sentinel.retention.SentinelGuard
import java.lang.reflect.Method

object SentinelRunner {

    fun run(
        sentinel: Sentinel,
        method: Method,
        block: () -> Unit,
    ) {
        val annotation = method.getAnnotation(SentinelGuard::class.java)
            ?: return block()

        val report = sentinel.inspect()

        if (report.riskLevel >= annotation.minRiskLevel) {
            when (annotation.action) {
                EXCEPTION -> error("Security violation detected")
                BLOCK -> return
                LOG -> println("Security warning")
            }
        } else {
            block()
        }
    }
}
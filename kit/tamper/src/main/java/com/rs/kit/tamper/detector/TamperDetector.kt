package com.rs.kit.tamper.detector

import android.content.Context
import com.rs.sentinel.detector.SecurityDetector
import com.rs.sentinel.ext.toHexString
import com.rs.sentinel.model.Threat
import com.rs.sentinel.type.SecurityType

class TamperDetector(
    private val context: Context,
    private val packageName: List<Byte>?,
    private val signature: List<Byte>?,
) : SecurityDetector {

    init {
        System.loadLibrary("sentinel-tamper")
    }

    private external fun verifyIntegrity(
        context: Context,
        expectedPackage: String,
        expectedSignature: String,
    ): Boolean

    override fun detect(): Threat? = when {
        isParamsNotBlank() && checkSecurity() -> {
            Threat(
                type = SecurityType.TAMPER,
                description = "Package tampering.",
                severity = 100
            )
        }

        else -> null
    }

    private fun isParamsNotBlank(): Boolean =
        packageName?.isNotEmpty() == true && signature?.isNotEmpty() == true

    private fun checkSecurity(): Boolean = verifyIntegrity(
        context = context,
        expectedPackage = packageName.orEmpty().toHexString(),
        expectedSignature = signature.orEmpty().toHexString()
    )
}
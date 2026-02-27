package com.rs.kit.tamper.detector

import android.content.Context
import com.rs.sentinel.detector.SecurityDetector
import com.rs.sentinel.model.Threat
import com.rs.sentinel.violation.SecurityViolation

class TamperDetector(
    private val context: Context,
    private val packageName: List<Byte>?,
    private val signature: List<Byte>?,
) : SecurityDetector {

    init {
        System.loadLibrary("sentinel-tamper")
    }

    private external fun verifyPackage(
        context: Context,
        expectedPackage: ByteArray,
    ): Boolean

    private external fun verifySignature(
        context: Context,
        expectedPackage: ByteArray,
        expectedSignature: ByteArray,
    ): Boolean

    override fun detect(): Threat? = when {
        isPackageValid() -> {
            Threat(
                violation = SecurityViolation.Tamper.PackageNameChanged
            )
        }

        isSignatureValid() -> {
            Threat(
                violation = SecurityViolation.Tamper.SignatureMismatch
            )
        }

        else -> null
    }

    private fun isPackageValid(): Boolean = verifyPackage(
        context = context,
        expectedPackage = packageName.orEmpty().toByteArray()
    )

    private fun isSignatureValid(): Boolean = verifySignature(
        context = context,
        expectedPackage = packageName.orEmpty().toByteArray(),
        expectedSignature = signature.orEmpty().toByteArray()
    )
}
package com.rs.kit.tamper.detector

import android.content.Context
import com.rs.sentinel.detector.SecurityDetector
import com.rs.sentinel.detector.Threat
import com.rs.sentinel.violation.SecurityViolation

class TamperDetector(
    private val context: Context,
    private val packageName: List<Byte>?,
    private val packageSignature: List<Byte>?,
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
        expectedPackageSignature: ByteArray,
    ): Boolean

    override fun detect(): List<Threat> = buildList {
        if (!isPackageValid()) {
            add(
                Threat(
                    violation = SecurityViolation.Tamper.PackageNameChanged
                )
            )
        }

        if (!isSignatureValid()) {
            add(
                Threat(
                    violation = SecurityViolation.Tamper.SignatureMismatch
                )
            )
        }
    }

    private fun isPackageValid(): Boolean = verifyPackage(
        context = context,
        expectedPackage = packageName.orEmpty().toByteArray()
    )

    private fun isSignatureValid(): Boolean = verifySignature(
        context = context,
        expectedPackage = packageName.orEmpty().toByteArray(),
        expectedPackageSignature = packageSignature.orEmpty().toByteArray()
    )
}
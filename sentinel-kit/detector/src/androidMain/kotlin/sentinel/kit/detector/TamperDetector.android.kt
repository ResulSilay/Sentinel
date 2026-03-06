package sentinel.kit.detector

import android.content.Context
import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat
import sentinel.core.violation.SecurityViolation

actual class TamperDetector actual constructor(
    private val context: Any?,
    private val appId: List<Byte>?,
    private val appSignature: List<Byte>?,
) : SecurityDetector {

    private val androidContext: Context
        get() = context as? Context
            ?: throw IllegalArgumentException("TamperDetector requires a valid Android Context")

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

    actual override fun detect(): List<Threat> = buildList {
        if (!isPackageValid()) {
            add(
                element = Threat(
                    violation = SecurityViolation.Tamper.PackageNameChanged
                )
            )
        }

        if (!isSignatureValid()) {
            add(
                element = Threat(
                    violation = SecurityViolation.Tamper.SignatureMismatch
                )
            )
        }
    }

    private fun isPackageValid(): Boolean = verifyPackage(
        context = androidContext,
        expectedPackage = appId.orEmpty().toByteArray()
    )

    private fun isSignatureValid(): Boolean = verifySignature(
        context = androidContext,
        expectedPackage = appId.orEmpty().toByteArray(),
        expectedPackageSignature = appSignature.orEmpty().toByteArray()
    )
}
package sentinel

import android.content.Context
import sentinel.core.detector.SecurityDetector
import sentinel.identity.Identity
import sentinel.kit.detector.DebugDetector
import sentinel.kit.detector.EmulatorDetector
import sentinel.kit.detector.HookDetector
import sentinel.kit.detector.RootDetector
import sentinel.kit.detector.TamperDetector

actual class Builder {

    private var context: Context? = null
    private val detectors = mutableListOf<SecurityDetector>()
    private val config = Config()

    internal fun setContext(context: Context): Builder = apply {
        this.context = context
    }

    actual fun config(block: Config.() -> Unit): Config = config.apply(block = block)

    actual fun root(): Builder = apply {
        detectors.add(element = RootDetector(context = context))
    }

    actual fun tamper(): Builder = apply {
        detectors.add(
            element = TamperDetector(
                context = context,
                appId = config.appId,
                appSignature = config.signature
            )
        )
    }

    actual fun hook(): Builder = apply {
        detectors.add(element = HookDetector())
    }

    actual fun emulator(): Builder = apply {
        detectors.add(element = EmulatorDetector())
    }

    actual fun debug(): Builder = apply {
        detectors.add(element = DebugDetector(context = context))
    }

    actual fun all(): Builder = apply {
        root()
        tamper()
        hook()
        emulator()
        debug()
    }

    actual fun build(): Sentinel = Sentinel(detectors = detectors, config = config)
}

fun Sentinel.Companion.configure(
    context: Context,
    block: Builder.() -> Unit,
): Sentinel {
    Sentinel.Identity = lazy { Identity(context = context) }.value

    return Builder()
        .setContext(context = context)
        .apply(block = block)
        .build()
}
package sentinel

import sentinel.core.detector.SecurityDetector
import sentinel.identity.Identity
import sentinel.kit.detector.DebugDetector
import sentinel.kit.detector.EmulatorDetector
import sentinel.kit.detector.HookDetector
import sentinel.kit.detector.RootDetector
import sentinel.kit.detector.TamperDetector

actual class Builder {

    private val detectors = mutableListOf<SecurityDetector>()
    private val config = Config()

    actual fun config(block: Config.() -> Unit): Config = config.apply(block = block)

    actual fun root(): Builder = apply {
        detectors.add(element = RootDetector())
    }

    actual fun tamper(): Builder = apply {
        detectors.add(element = TamperDetector())
    }

    actual fun hook(): Builder = apply {
        detectors.add(element = HookDetector())
    }

    actual fun emulator(): Builder = apply {
        detectors.add(element = EmulatorDetector())
    }

    actual fun debug(): Builder = apply {
        detectors.add(element = DebugDetector())
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
    block: Builder.() -> Unit,
): Sentinel {
    Sentinel.Identity = lazy { Identity() }.value

    return Builder()
        .apply(block = block)
        .build()
}
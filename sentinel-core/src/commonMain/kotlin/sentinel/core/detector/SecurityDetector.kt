package sentinel.core.detector

interface SecurityDetector {

    fun detect(): List<Threat>?
}
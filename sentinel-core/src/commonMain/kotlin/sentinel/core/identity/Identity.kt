package sentinel.core.identity

interface Identity {
    val deviceId: String
    val appId: String
    val signature: String?
    val hash: String?
    val platform: String
}
package sentinel

expect class Builder {

    fun config(block: Config.() -> Unit): Config

    fun root(): Builder

    fun tamper(): Builder

    fun hook(): Builder

    fun emulator(): Builder

    fun debug(): Builder

    fun all(): Builder

    fun build(): Sentinel
}
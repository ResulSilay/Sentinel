package com.rs.sentinel.reflect

import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor
import com.rs.sentinel.violation.SecurityViolation

internal fun getAllViolations(): List<SecurityViolation> {
    val result = mutableListOf<SecurityViolation>()

    fun addSubclasses(kClass: KClass<out SecurityViolation>) {
        if (!kClass.isSealed) return

        kClass.sealedSubclasses.forEach { sub ->
            sub.objectInstance?.let {
                result.add(it)
                return@forEach
            }

            sub.primaryConstructor?.let { ctor ->
                val params = ctor.parameters
                val args = params.associateWith { param ->
                    param.defaultValueOrNull()
                }
                val instance = try {
                    ctor.callBy(args)
                } catch (_: Exception) {
                    null
                }
                instance?.let { result.add(it) } ?: run {
                    if (sub.isSubclassOf(SecurityViolation::class)) {
                        addSubclasses(sub)
                    }
                }
            } ?: run {
                if (sub.isSubclassOf(SecurityViolation::class)) {
                    addSubclasses(sub)
                }
            }
        }
    }

    addSubclasses(SecurityViolation::class)
    return result
}

internal fun getDynamicGroupName(violation: SecurityViolation): String {
    var cls: Class<*>? = violation.javaClass

    while (cls != null && cls != SecurityViolation::class.java) {
        val parent = cls.enclosingClass ?: cls.superclass

        if (parent != null && parent != SecurityViolation::class.java &&
            SecurityViolation::class.java.isAssignableFrom(parent) &&
            parent != cls
        ) {
            return parent.simpleName
        }

        cls = parent
    }

    return "Unknown"
}

@Suppress("SameReturnValue")
private fun kotlin.reflect.KParameter.defaultValueOrNull(): Any? {
    return if (this.isOptional) null else null
}
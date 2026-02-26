package com.rs.sentinel.ext

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import java.security.MessageDigest

fun Context.getAppPackageName(): String = packageName.orEmpty().also {
    Log.i("Sentinel", it.toByteList().toString())
}

fun Context.getAppSignatureSHA256(): String {
    val packageInfo: PackageInfo = try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNING_CERTIFICATES
            )
        } else {
            @Suppress("DEPRECATION")
            packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES
            )
        }
    } catch (e: PackageManager.NameNotFoundException) {
        throw RuntimeException("Package not found: $packageName", e)
    }

    val signatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        packageInfo.signingInfo?.apkContentsSigners
    } else {
        @Suppress("DEPRECATION")
        packageInfo.signatures
    }

    if (signatures.isNullOrEmpty()) {
        throw RuntimeException("No signatures found for package: $packageName")
    }

    val cert = signatures[0]
    val md = MessageDigest.getInstance("SHA-256")
    val hash = md.digest(cert.toByteArray())

    return hash.joinToString("") { "%02X".format(it) }.also {
        Log.i("Sentinel", it.toByteList().toString())
    }
}
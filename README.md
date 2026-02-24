# Sentinel

Lightweight Android Security Toolkit for protecting apps against tampering, reverse engineering, rooted devices, and insecure runtime environments.

## Overview

**Sentinel** is a lightweight and modular Android security toolkit designed to detect insecure runtime environments such as:

- Rooted devices
- Emulators
- Debugging sessions
- Hooking frameworks

It performs deep environmental analysis, calculates a unified risk score, and provides a detailed security report to help protect your application.

## Features

- 🔹 Modular detector architecture
- 🔹 Unified risk scoring system
- 🔹 Configurable threat threshold
- 🔹 DSL-style configuration API
- 🔹 Detailed security reporting
- 🔹 Lightweight and high performance

## Quick Start

### Basic Configuration

Sentinel uses a centralized DSL configuration to manage all security checks.

```kotlin
val sentinel = Sentinel.configure(context) {
    all() // Enables Root, Emulator, Debug, and Hook detection
}
```

Instead of basic checks, Sentinel performs a thorough inspection of the environment and provides a detailed report based on threat severity.

```kotlin

val report = sentinel.inspect()

if (report.riskLevel == RiskLevel.HIGH) {
    println("Device environment is compromised")
}
```

## Root Detection
Checks if the Android device is rooted or has superuser binaries (SU, Magisk, etc.).

### Usage
```kotlin
val sentinel = Sentinel.configure(context) { 
    root() 
}

val report = sentinel.inspect()

if (report.hasThreatType(SecurityType.ROOT)) {
    // Handle root threat
}
```

## Summary

- **`inspect()`** → Performs a deep environmental analysis using all active detectors.
- **`isSafe()`** → Returns `true` only if the risk level is `SAFE` (total score is 0).
- **`isCritical()`** → Returns `true` if the score meets or exceeds the defined threshold (`HIGH`).
- **`hasThreatType(type)`** → Checks if a specific threat type (ROOT, EMULATOR, DEBUG, HOOK) was detected in the scan.
- **`riskLevel`** → Returns the categorized status: `SAFE`, `MEDIUM`, or `HIGH` based on the score and threshold.

## Installation

Add JitPack repository:

```gradle
dependencyResolutionManagement {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

Include the library in your **app module** `build.gradle`:

```gradle
implementation("com.github.ResulSilay:Sentinel:1.1.0.jitpack.beta")
```
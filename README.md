<div align="center">

<p align="center">
  <img src="/art/logo.png" alt="Logo" width="200"/>
</p>

[![Android](https://img.shields.io/badge/Android-000000?style=for-the-badge&logo=android&logoColor=ffffff)](https://developer.android.com/)
[![Security](https://img.shields.io/badge/Security-000000?style=for-the-badge&logo=bitwarden)](#)
[![Toolkit](https://img.shields.io/badge/Toolkit-000000?style=for-the-badge&logo=hackthebox&logoColor=ffffff)](#)
[![Gradle](https://img.shields.io/badge/Gradle-000000?style=for-the-badge&logo=gradle)](#)
[![Version](https://img.shields.io/badge/1.1.0.beta-000000?style=for-the-badge&logo=stackblitz)](#)

Lightweight Android Security Toolkit for protecting apps against tampering, reverse engineering, rooted devices, and insecure runtime environments.

<!--
<a href="https://play.google.com/store/apps/details?id=com.rs.sentinel.app">
    <img src="art/google-play-badge.png" width="200" alt="sentinel"/>
</a>
-->

<p align="center">
  <img alt="image" src="art/header.png" />
</p>

</div>
<br>

## Overview

**Sentinel** is a lightweight and modular Android security toolkit designed to detect insecure runtime environments such as:

- Rooted devices
- Emulators
- Debugging sessions
- Hooking frameworks

It performs deep environmental analysis, calculates a unified risk severity, and provides a detailed security report to help protect your application.

## Features

- ♦️ Modular detector architecture – easily extendable with different security checks
- ♦️ Total severity-based risk assessment system
- ♦️ Configurable threat threshold
- ♦️ DSL-style configuration API
- ♦️ Detailed security reporting
- ♦️ Lightweight and high performance

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

- **Comprehensive environment scanning:** Detects threats such as Root, Emulator, Debugger, Hook, and Mock Location.
- **Total severity-based risk assessment:** All detected threats’ severity values are summed to calculate the system’s risk level.
- **Risk Levels:** SAFE, LOW, MEDIUM, HIGH.
- **Easy logging:** Log all threats and the overall risk level effortlessly.

## API

- **`inspect()`** → Performs a full system security analysis using all active detectors and returns a `SecurityReport` containing detected threats and their total severity.
- **`isSafe()`** → Returns `true` only if no threats are detected (`totalSeverity == 0`).
- **`isCritical()`** → Returns `true` if the total severity exceeds the defined threshold, indicating `HIGH` risk.

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
implementation("com.github.ResulSilay:Sentinel:1.2.1.jitpack.beta")
```

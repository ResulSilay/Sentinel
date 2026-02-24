package com.rs.sentinel.detector

import com.rs.sentinel.model.Threat

interface SecurityDetector {

    fun detect(): Threat?
}
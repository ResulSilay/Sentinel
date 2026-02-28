package com.rs.sentinel.detector

interface SecurityDetector {

    fun detect(): List<Threat>?
}
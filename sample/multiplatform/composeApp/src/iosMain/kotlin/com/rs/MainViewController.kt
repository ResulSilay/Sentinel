package com.rs

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import sentinel.Sentinel
import sentinel.configure

fun MainViewController() = ComposeUIViewController {

    val sentinel = remember {
        Sentinel.configure {
            config {
                this.threshold = 20
            }

            all()
        }
    }

    App(
        sentinel = sentinel,
        appId = Sentinel.Identity.appId
    )
}
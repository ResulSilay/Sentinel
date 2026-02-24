package com.rs.sentinel.core

import android.content.Context
import com.rs.kit.root.core.RootImpl

class Sentinel private constructor() {

    companion object Root {

        @JvmStatic
        fun create(context: Context) = RootImpl(context = context.applicationContext)
    }
}
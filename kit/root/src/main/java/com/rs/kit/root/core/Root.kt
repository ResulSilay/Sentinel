package com.rs.kit.root.core

import com.rs.kit.root.model.RootReport

interface Root {

    fun isRooted(): Boolean

    fun getReport(): RootReport
}
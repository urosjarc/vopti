package com.urosjarc.vopti.shared

import java.util.*

@JvmInline
value class Id<T>(val value: UUID = UUID.randomUUID()) {
    override fun toString(): String = this.value.toString()
}

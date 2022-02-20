package com.emorae.cache.data

import java.util.*

data class Register(
    val value: MutableSet<Pair<Int,String>> = mutableSetOf(),
    val expiration: Date? = null)
package com.emorae.services

import com.emorae.cache.MiniRedisImpl
import com.emorae.cache.data.Register
import kotlinx.coroutines.sync.Mutex

class ServiceComposer(
    cache: MutableMap<String, Register>,
    mutex: Mutex
) {
    val miniRedis = MiniRedisImpl(cache,mutex)
}
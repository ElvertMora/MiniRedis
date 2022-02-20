package com.emorae.cache

import com.emorae.cache.data.Option

interface MiniRedis {

    val dbSize: Int

    suspend fun set(key: String, value: String, option: Option?, duration: Long?): String

    suspend fun get(key: String): String

    suspend fun del(keys: List<String>): Int

    suspend fun incr(key: String): String

    suspend fun zAdd(key: String, vararg args: Pair<Int,String>): String

    fun zCard(key: String): String

    fun zRank(key: String, member: String): String

    fun zRange(key: String, start: Int?, stop: Int?): String

    fun clear()
}
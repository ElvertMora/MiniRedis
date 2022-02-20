package com.emorae.cache

import com.emorae.cache.data.Option

interface MiniRedis {

    val dbSize: Int

    fun set(key: String, value: String, option: Option?, duration: Long?): String

    fun get(key: String): String

    fun del(keys: List<String>): Int

    fun incr(key: String): String

    fun zAdd(key: String, vararg args: Pair<Int,String>): String

    fun zCard(key: String): String

    fun zRank(key: String, member: String): String

    fun zRange(key: String, start: Int?, stop: Int?): String

    fun clear()
}
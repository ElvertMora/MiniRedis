package com.emorae.cache

import com.emorae.cache.data.Option
import com.emorae.cache.data.Register
import java.time.Duration
import java.util.Date

class MiniRedisImpl : MiniRedis {

    private val cache = mutableMapOf<String, Register>()

    override val dbSize: Int
        get() = cache.size

    override fun set(key: String, value: String, option: Option?, duration: Long?): String {
        if ((option != null) && (option in listOf(
                Option.EX,
                Option.PX,
                Option.EXAT,
                Option.PXAT
            )) && (duration == null)
        ) {
            return NIL
        } else {
            val valueSet = mutableSetOf(Pair(1, value))
            when (option) {
                Option.EX -> cache[key] =
                    Register(
                        value = valueSet,
                        expiration = Date(Date().time + Duration.ofSeconds(duration!!).toMillis())
                    )
                Option.PX -> cache[key] =
                    Register(value = mutableSetOf(Pair(1, value)), expiration = Date(Date().time + duration!!))
                Option.EXAT -> cache[key] =
                    Register(value = valueSet, expiration = Date(Duration.ofSeconds(duration!!).toMillis()))
                Option.PXAT -> cache[key] = Register(value = valueSet, expiration = Date(duration!!))
                Option.NX -> if (!cache.containsKey(key)) cache[key] = Register(value = valueSet) else return NIL
                Option.XX -> if (cache.containsKey(key)) cache[key] = Register(value = valueSet) else return NIL
                Option.KEEPTTL -> if (cache.containsKey(key)) cache[key] = cache[key]!!.copy(value = valueSet)
                Option.GET -> {
                    val oldValue = cache[key]?.value?.first()?.second
                    cache[key] = Register(value = valueSet)
                    return oldValue ?: NIL
                }
                null -> cache[key] = Register(value = valueSet)
            }
            return "OK"
        }
    }

    override fun get(key: String): String {
        if (!cache.containsKey(key)) {
            return NIL
        }
        val expiration = cache[key]?.expiration
        val value = cache[key]?.value?.first()?.second ?: NIL
        return when {
            expiration == null -> value
            expiration.after(Date()) -> value
            else -> {
                cache.remove(key)
                NIL
            }
        }
    }

    override fun del(keys: List<String>): Int = keys.map(this::remove).sum()

    private fun remove(key: String): Int {
        if (cache.containsKey(key)) {
            cache.remove(key)
            return 1
        }
        return 0
    }

    override fun incr(key: String): String {
        if (cache.containsKey(key)) {
            val value = cache[key]!!.value.first().second
            return try {
                val newValue = value.toInt().plus(1)
                cache[key] = cache[key]!!.copy(value = mutableSetOf(Pair(1, "$newValue")))
                "$newValue"
            } catch (e: NumberFormatException) {
                "the value not is a number: $value"
            }
        }
        cache[key] = Register(mutableSetOf(Pair(1, "1")))
        return "1"
    }

    override fun zAdd(key: String, vararg args: Pair<Int, String>): String {
        if (cache.containsKey(key)) {
            val register = cache[key]
            register?.value?.addAll(args)
        } else {
            val newRegister = Register()
            newRegister.value.addAll(args)
            cache[key] = newRegister
        }
        return "${args.size}"
    }

    override fun zCard(key: String): String {
        return "${cache[key]?.value?.size}"
    }

    override fun zRank(key: String, member: String): String {
        val element = cache[key]?.value?.find { pair -> pair.second == member }
        val index = cache[key]?.value?.indexOf(element)
        return if (index == null) NIL else "$index"
    }

    override fun zRange(key: String, start: Int?, stop: Int?): String {
        val value = cache[key]?.value ?: emptySet()
        if (value.isEmpty()) return NIL
        val orderCollection = value.sortedBy { pair -> pair.second }.sortedBy { pair -> pair.first }
        val indexStart = if (start == null) {
            0
        } else if (start >= orderCollection.size) {
            orderCollection.size
        } else if (start < 0) {
            orderCollection.size + start
        } else {
            start
        }
        val indexStop = if (stop == null || stop >= orderCollection.size) {
            orderCollection.size
        } else if (stop < 0) {
            orderCollection.size + stop + 1
        } else {
            stop + 1
        }
        val returnList = orderCollection.subList(indexStart, indexStop)
        if (returnList.isEmpty()) return NIL
        return returnList.mapIndexed { inx, pair -> "${inx + 1}) ${pair.second}" }.joinToString(separator = "\n")
    }

    override fun clear() {
        cache.clear()
    }

    companion object{
        private const val NIL = "nil"
    }

}
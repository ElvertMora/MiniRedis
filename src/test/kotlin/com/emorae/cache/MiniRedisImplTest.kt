package com.emorae.cache

import com.emorae.cache.data.Option
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MiniRedisImplTest {

    private var miniRedis: MiniRedis = MiniRedisImpl(mutableMapOf(), Mutex())

    @Test
    fun getDbSize() {
        Assertions.assertTrue(miniRedis.dbSize == 0)
    }

    @Test
    fun set() = runBlocking {
        miniRedis.set("any", "other", null, null)
        Assertions.assertEquals("other", miniRedis.get("any"))
        miniRedis.set("any-2", "other", Option.EX, 1)
        Assertions.assertEquals("other", miniRedis.get("any-2"))
        miniRedis.set("any-3", "other", Option.PX, 1)
        Assertions.assertEquals("other", miniRedis.get("any-3"))
        miniRedis.set("any-4", "other", Option.PXAT, 1)
        Assertions.assertEquals("nil", miniRedis.get("any-4"))
        miniRedis.set("any-5", "other", Option.EXAT, 1)
        Assertions.assertEquals("nil", miniRedis.get("any-5"))
        miniRedis.set("any-6", "other", Option.EX, null)
        Assertions.assertEquals("nil", miniRedis.get("any-6"))
        Assertions.assertEquals("nil", miniRedis.set("any", "other-2", Option.NX, null))
        Assertions.assertEquals("OK", miniRedis.set("any-7", "other-2", Option.NX, null))
        Assertions.assertEquals("other-2", miniRedis.get("any-7"))
        Assertions.assertEquals("OK", miniRedis.set("any", "other-3", Option.XX, null))
        Assertions.assertEquals("other-3", miniRedis.get("any"))
        Assertions.assertEquals("nil", miniRedis.set("any-8", "other", Option.XX, null))
        Assertions.assertEquals("other-3", miniRedis.set("any", "other-4", Option.GET, null))
        Assertions.assertEquals("other-4", miniRedis.get("any"))
        Assertions.assertEquals("nil", miniRedis.set("any-9", "other-9", Option.GET, null))
        Assertions.assertEquals("other-9", miniRedis.get("any-9"))
        Assertions.assertEquals("OK", miniRedis.set("any-9", "other-9", Option.KEEPTTL, null))
    }

    @Test
    fun get() = runBlocking {
        Assertions.assertEquals("nil", miniRedis.get("any"))
    }

    @Test
    fun delTheKeyDontExist() = runBlocking {
        Assertions.assertEquals(0, miniRedis.del(listOf("any")))
    }

    @Test
    fun delTheKeyExist() = runBlocking {
        miniRedis.set("any", "other", null, null)
        Assertions.assertEquals(1, miniRedis.del(listOf("any")))
    }

    @Test
    fun incr() = runBlocking {
        miniRedis.set("any", "1", null, null)
        Assertions.assertEquals("2", miniRedis.incr("any"))
    }

    @Test
    fun incrNotNumberValue() = runBlocking {
        miniRedis.set("any", "other", null, null)
        Assertions.assertEquals("the value not is a number: other", miniRedis.incr("any"))
    }

    @Test
    fun incrNotExitKey() = runBlocking {
        Assertions.assertEquals("1", miniRedis.incr("any"))
    }

    @Test
    fun zAdd() = runBlocking {
        miniRedis.zAdd("any", Pair(1, "one"))
        miniRedis.zAdd("any", Pair(1, "uno"))
        miniRedis.zAdd("any", Pair(2, "two"), Pair(3, "three"))
        Assertions.assertEquals("4", miniRedis.zCard("any"))
    }

    @Test
    fun zCard() = runBlocking {
        miniRedis.set("any", "other", null, null)
        Assertions.assertEquals("1", miniRedis.zCard("any"))
    }

    @Test
    fun zRank() = runBlocking {
        miniRedis.set("any", "other", null, null)
        Assertions.assertEquals("0", miniRedis.zRank("any", "other"))
        Assertions.assertEquals("nil", miniRedis.zRank("any-2", "other"))
    }

    @Test
    fun zRange() = runBlocking {
        Assertions.assertEquals("nil", miniRedis.zRange("any", null, null))
        miniRedis.zAdd("any", Pair(2, "two"), Pair(3, "three"))
        miniRedis.zAdd("any", Pair(1, "one"))
        Assertions.assertEquals("1) one\n2) two\n3) three", miniRedis.zRange("any", null, null))
        Assertions.assertEquals("1) one\n2) two\n3) three", miniRedis.zRange("any", 0, -1))
        Assertions.assertEquals("1) two\n2) three", miniRedis.zRange("any", 1, 2))
        Assertions.assertEquals("1) three", miniRedis.zRange("any", 2, 3))
        Assertions.assertEquals("1) three", miniRedis.zRange("any", 2, null))
        Assertions.assertEquals("1) two\n2) three", miniRedis.zRange("any", -2, -1))
        Assertions.assertEquals("nil", miniRedis.zRange("any", -1, -2))
        Assertions.assertEquals("nil", miniRedis.zRange("any", 4, null))
        miniRedis.clear()
        Assertions.assertEquals("nil", miniRedis.zRange("any", -1, -2))
    }
}
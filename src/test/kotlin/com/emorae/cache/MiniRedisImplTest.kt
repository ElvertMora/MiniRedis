package com.emorae.cache

import com.emorae.cache.data.Option
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class MiniRedisImplTest {

    private lateinit var miniRedis: MiniRedis

    @Before
    fun init() {
        miniRedis = MiniRedisImpl()
    }

    @Test
    fun getDbSize() {
        Assert.assertTrue(miniRedis.dbSize == 0)
    }

    @Test
    fun set() {
        miniRedis.set("any", "other", null, null)
        Assert.assertEquals("other", miniRedis.get("any"))
        miniRedis.set("any-2", "other", Option.EX, 1)
        Assert.assertEquals("other", miniRedis.get("any-2"))
        miniRedis.set("any-3", "other", Option.PX, 1)
        Assert.assertEquals("other", miniRedis.get("any-3"))
        miniRedis.set("any-4", "other", Option.PXAT, 1)
        Assert.assertEquals("nil", miniRedis.get("any-4"))
        miniRedis.set("any-5", "other", Option.EXAT, 1)
        Assert.assertEquals("nil", miniRedis.get("any-5"))
        miniRedis.set("any-6", "other", Option.EX, null)
        Assert.assertEquals("nil", miniRedis.get("any-6"))
        Assert.assertEquals("nil",miniRedis.set("any", "other-2", Option.NX, null))
        Assert.assertEquals("OK",miniRedis.set("any-7", "other-2", Option.NX, null))
        Assert.assertEquals("other-2", miniRedis.get("any-7"))
        Assert.assertEquals("OK",miniRedis.set("any", "other-3", Option.XX, null))
        Assert.assertEquals("other-3", miniRedis.get("any"))
        Assert.assertEquals("nil",miniRedis.set("any-8", "other", Option.XX, null))
        Assert.assertEquals("other-3",miniRedis.set("any", "other-4", Option.GET, null))
        Assert.assertEquals("other-4", miniRedis.get("any"))
        Assert.assertEquals("nil",miniRedis.set("any-9", "other-9", Option.GET, null))
        Assert.assertEquals("other-9", miniRedis.get("any-9"))
        Assert.assertEquals("OK",miniRedis.set("any-9", "other-9", Option.KEEPTTL, null))

    }

    @Test
    fun get() {
        Assert.assertEquals("nil", miniRedis.get("any"))
    }

    @Test
    fun delTheKeyDontExist() {
        Assert.assertEquals(0, miniRedis.del(listOf("any")))
    }

    @Test
    fun delTheKeyExist() {
        miniRedis.set("any", "other", null, null)
        Assert.assertEquals(1, miniRedis.del(listOf("any")))
    }

    @Test
    fun incr() {
        miniRedis.set("any", "1", null, null)
        Assert.assertEquals("2", miniRedis.incr("any"))
    }

    @Test
    fun incrNotNumberValue() {
        miniRedis.set("any", "other", null, null)
        Assert.assertEquals("the value not is a number: other", miniRedis.incr("any"))
    }

    @Test
    fun incrNotExitKey() {
        Assert.assertEquals("1", miniRedis.incr("any"))
    }

    @Test
    fun zAdd() {
        miniRedis.zAdd("any", Pair(1, "one"))
        miniRedis.zAdd("any", Pair(1, "uno"))
        miniRedis.zAdd("any", Pair(2, "two"), Pair(3, "three"))
        Assert.assertEquals("4", miniRedis.zCard("any"))
    }

    @Test
    fun zCard() {
        miniRedis.set("any", "other", null, null)
        Assert.assertEquals("1", miniRedis.zCard("any"))
    }

    @Test
    fun zRank() {
        miniRedis.set("any", "other", null, null)
        Assert.assertEquals("0", miniRedis.zRank("any", "other"))
        Assert.assertEquals("nil", miniRedis.zRank("any-2", "other"))
    }

    @Test
    fun zRange() {
        Assert.assertEquals("nil", miniRedis.zRange("any", null, null))
        miniRedis.zAdd("any", Pair(2, "two"), Pair(3, "three"))
        miniRedis.zAdd("any", Pair(1, "one"))
        Assert.assertEquals("1) one\n2) two\n3) three", miniRedis.zRange("any", null, null))
        Assert.assertEquals("1) one\n2) two\n3) three", miniRedis.zRange("any", 0, -1))
        Assert.assertEquals("1) two\n2) three", miniRedis.zRange("any", 1, 2))
        Assert.assertEquals("1) three", miniRedis.zRange("any", 2, 3))
        Assert.assertEquals("1) three", miniRedis.zRange("any", 2, null))
        Assert.assertEquals("1) two\n2) three", miniRedis.zRange("any", -2, -1))
        Assert.assertEquals("nil", miniRedis.zRange("any", -1, -2))
        Assert.assertEquals("nil", miniRedis.zRange("any", 4, null))
        miniRedis.clear()
        Assert.assertEquals("nil", miniRedis.zRange("any", -1, -2))
    }

    @Test
    fun clear() {
    }
}
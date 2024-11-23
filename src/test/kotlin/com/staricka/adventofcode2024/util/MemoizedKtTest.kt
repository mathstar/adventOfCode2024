package com.staricka.adventofcode2024.util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

class MemoizedKtTest {
    @Test
    fun oneParamTest() {
        val called = BooleanArray(2)
        val function = {i: Int ->
            if (called[i]) {
                fail("Unexpected call to function with $i")
            } else {
                called[i] = true
            }
            i
        }

        val memoized = memoized(function)
        assertEquals(0, memoized(0))
        assertEquals(0, memoized(0))
        assertEquals(1, memoized(1))
        assertEquals(1, memoized(1))
        assertArrayEquals(booleanArrayOf(true, true), called)
    }

    @Test
    fun oneParamInlineTest() {
        val called = BooleanArray(2)
        val function = memoized {i: Int ->
            if (called[i]) {
                fail("Unexpected call to function with $i")
            } else {
                called[i] = true
            }
            i
        }

        assertEquals(0, function(0))
        assertEquals(0, function(0))
        assertEquals(1, function(1))
        assertEquals(1, function(1))
        assertArrayEquals(booleanArrayOf(true, true), called)
    }

    @Test
    fun twoParamTest() {
        val called = HashMap<TwoMemo<Int, Int>, Boolean>()
        val function = {p0: Int, p1: Int ->
            if (called[TwoMemo(p0, p1)] == true) {
                fail("Unexpected call to function with $p0, $p1")
            } else {
                called[TwoMemo(p0, p1)] = true
            }
            p0 + p1
        }

        val memoized = memoized(function)
        assertEquals(3, memoized(1, 2))
        assertEquals(3, memoized(1, 2))
        assertEquals(5, memoized(2, 3))
        assertEquals(5, memoized(2, 3))
        assertEquals(mapOf(
            TwoMemo(1, 2) to true,
            TwoMemo(2, 3) to true
        ), called)
    }

    @Test
    fun threeParamTest() {
        val called = HashMap<ThreeMemo<Int, Int, Int>, Boolean>()
        val function = {p0: Int, p1: Int, p2: Int ->
            if (called[ThreeMemo(p0, p1, p2)] == true) {
                fail("Unexpected call to function with $p0, $p1, $p2")
            } else {
                called[ThreeMemo(p0, p1, p2)] = true
            }
            p0 + p1 + p2
        }

        val memoized = memoized(function)
        assertEquals(6, memoized(1, 2, 3))
        assertEquals(6, memoized(1, 2, 3))
        assertEquals(9, memoized(2, 3, 4))
        assertEquals(9, memoized(2, 3, 4))
        assertEquals(mapOf(
            ThreeMemo(1, 2, 3) to true,
            ThreeMemo(2, 3, 4) to true
        ), called)
    }

    @Test
    fun fourParamTest() {
        val called = HashMap<FourMemo<Int, Int, Int, Int>, Boolean>()
        val function = {p0: Int, p1: Int, p2: Int, p3: Int ->
            if (called[FourMemo(p0, p1, p2, p3)] == true) {
                fail("Unexpected call to function with $p0, $p1, $p2, $p3")
            } else {
                called[FourMemo(p0, p1, p2, p3)] = true
            }
            p0 + p1 + p2 + p3
        }

        val memoized = memoized(function)
        assertEquals(10, memoized(1, 2, 3, 4))
        assertEquals(10, memoized(1, 2, 3, 4))
        assertEquals(100, memoized(10, 20, 30, 40))
        assertEquals(100, memoized(10, 20, 30, 40))
        assertEquals(mapOf(
            FourMemo(1, 2, 3, 4) to true,
            FourMemo(10, 20, 30, 40) to true
        ), called)
    }
}
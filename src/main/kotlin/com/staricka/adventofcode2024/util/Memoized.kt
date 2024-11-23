package com.staricka.adventofcode2024.util

fun <I0, O> memoized(function: (I0) -> O): (I0) -> O {
    val memos = HashMap<I0, O>()
    return {p0 ->
        if (memos.contains(p0)) memos[p0]!! else function(p0).also { memos[p0] = it }
    }
}

data class TwoMemo<I0, I1>(val i0: I0, val i1: I1)
fun <I0, I1, O> memoized(function: (I0, I1) -> O): (I0, I1) -> O {
    val memos = HashMap<TwoMemo<I0, I1>, O>()
    return {p0, p1 ->
        val key = TwoMemo(p0, p1)
        if (memos.contains(key)) memos[key]!! else function(p0, p1).also { memos[key] = it }
    }
}

data class ThreeMemo<I0, I1, I2>(val i0: I0, val i1: I1, val i2: I2)
fun <I0, I1, I2, O> memoized(function: (I0, I1, I2) -> O): (I0, I1, I2) -> O {
    val memos = HashMap<ThreeMemo<I0, I1, I2>, O>()
    return {p0, p1, p2 ->
        val key = ThreeMemo(p0, p1, p2)
        if (memos.contains(key)) memos[key]!! else function(p0, p1, p2).also { memos[key] = it }
    }
}

data class FourMemo<I0, I1, I2, I3>(val i0: I0, val i1: I1, val i2: I2, val i3: I3)
fun <I0, I1, I2, I3, O> memoized(function: (I0, I1, I2, I3) -> O): (I0, I1, I2, I3) -> O {
    val memos = HashMap<FourMemo<I0, I1, I2, I3>, O>()
    return {p0, p1, p2, p3 ->
        val key = FourMemo(p0, p1, p2, p3)
        if (memos.contains(key)) memos[key]!! else function(p0, p1, p2, p3).also { memos[key] = it }
    }
}
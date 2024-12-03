package com.staricka.adventofcode2024.days

import com.staricka.adventofcode2024.framework.Day
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

abstract class DayTest(
    val day: () -> Day,
    val input: String,
    val part1Expected: Any?,
    val part2Expected: Any?,
    val part2Input: String = input
) {
    @Test
    fun part1() {
        assertEquals(part1Expected, day().part1(input))
    }

    @Test
    fun part2() {
        assertEquals(part2Expected, day().part2(part2Input))
    }
}
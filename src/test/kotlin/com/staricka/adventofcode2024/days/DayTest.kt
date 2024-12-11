package com.staricka.adventofcode2024.days

import com.staricka.adventofcode2024.framework.Day
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

abstract class DayTest(
    private val day: () -> Day,
    private val input: String,
    private val part1Expected: Any?,
    private val part2Expected: Any?,
    private val part2Input: String = input
) {
    @Test
    fun part1() {
        if (part1Expected != null) assertEquals(part1Expected, day().part1(input))
    }

    @Test
    fun part2() {
        if (part2Expected != null) assertEquals(part2Expected, day().part2(part2Input))
    }
}
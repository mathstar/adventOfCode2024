package com.staricka.adventofcode2024.days

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Day1Test {
    @Test
    fun part1() {
        assertEquals(
            11, Day1().part1(
                """
                3   4
                4   3
                2   5
                1   3
                3   9
                3   3
               """.trimIndent()
            )
        )
    }

    @Test
    fun part2() {
        assertEquals(
            31, Day1().part2(
                """
                3   4
                4   3
                2   5
                1   3
                3   9
                3   3
               """.trimIndent()
            )
        )
    }
}
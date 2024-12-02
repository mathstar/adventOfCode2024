package com.staricka.adventofcode2024.days

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Day2Test {
    @Test
    fun part1() {
        assertEquals(2, Day2().part1("""
            7 6 4 2 1
            1 2 7 8 9
            9 7 6 2 1
            1 3 2 4 5
            8 6 4 4 1
            1 3 6 7 9
            """.trimIndent()))
    }

    @Test
    fun part2() {
        assertEquals(4, Day2().part2("""
            7 6 4 2 1
            1 2 7 8 9
            9 7 6 2 1
            1 3 2 4 5
            8 6 4 4 1
            1 3 6 7 9
            """.trimIndent()))
    }
 }
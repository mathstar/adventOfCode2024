package com.staricka.adventofcode2024.days

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day4Test: DayTest(
    {Day4()},
    """
        MMMSXXMASM
        MSAMXMSMSA
        AMXSXMAAMM
        MSAMASMSMX
        XMASAMXAMM
        XXAMMXXAMA
        SMSMSASXSS
        SAXAMASAAA
        MAMMMXMMMM
        MXMXAXMASX
    """.trimIndent(),
    18,
    9,
) {
    @Test
    fun part1Small() {
        assertEquals(4, Day4().part1("""
            ..X...
            .SAMX.
            .A..A.
            XMAS.S
            .X....
        """.trimIndent()))
    }
}
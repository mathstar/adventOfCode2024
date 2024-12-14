package com.staricka.adventofcode2024.days

import com.staricka.adventofcode2024.framework.Day
import com.staricka.adventofcode2024.util.splitByBlankLines
import java.util.regex.Pattern
import kotlin.math.ceil
import kotlin.math.floor

class Day13: Day {
    data class Machine(val ax: Long, val ay: Long, val bx: Long, val by: Long, val px: Long, val py: Long) {
        fun calculateCostWithAlgebra(): Long? {
            val b: Double = (py.toDouble() / ay - px.toDouble() / ax) / (by.toDouble() / ay - bx.toDouble()/ax)
            if (b - floor(b) > 0.001 && ceil(b) - b > 0.001) return null
            val ib = if (b - floor(b) < 0.001) floor(b).toLong() else ceil(b).toLong()
            val a = (px - bx * ib) / ax
            return a * 3 + ib
        }

        fun add(m: Long) = Machine(ax, ay, bx, by, px + m, py + m)

        companion object {
            private val aPattern: Pattern = Pattern.compile("""Button A: X\+([0-9]+), Y\+([0-9]+)""")
            private val bPattern: Pattern = Pattern.compile("""Button B: X\+([0-9]+), Y\+([0-9]+)""")
            private val prizePattern: Pattern = Pattern.compile("""Prize: X=([0-9]+), Y=([0-9]+)""")
            fun fromString(input: String): Machine {
                val lines = input.lines()
                val aMatch = aPattern.matcher(lines[0]).apply { find() }
                val bMatch = bPattern.matcher(lines[1]).apply { find() }
                val prizeMatch = prizePattern.matcher(lines[2]).apply { find() }

                return Machine(aMatch.group(1).toLong(), aMatch.group(2).toLong(),
                    bMatch.group(1).toLong(), bMatch.group(2).toLong(),
                    prizeMatch.group(1).toLong(), prizeMatch.group(2).toLong())
            }
        }
    }

    override fun part1(input: String): Long {
        val machines = input.trim().splitByBlankLines().map { Machine.fromString(it) }
        return machines.sumOf { (it.calculateCostWithAlgebra() ?: 0) }
    }

    override fun part2(input: String): Long {
        val machines = input.trim().splitByBlankLines().map { Machine.fromString(it) }
        return machines.map { it.add(10000000000000) }.sumOf { it.calculateCostWithAlgebra() ?: 0 }
    }
}
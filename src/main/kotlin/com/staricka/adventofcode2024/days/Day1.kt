package com.staricka.adventofcode2024.days

import com.staricka.adventofcode2024.framework.Day
import kotlin.math.abs

class Day1: Day {
    override fun part1(input: String): Int {
        val splits = input.lines()
            .filter{ it.isNotBlank() }
            .map{
                it.split("   ")
            }
        val left = splits.map { it[0].toInt() }.sorted()
        val right = splits.map { it[1].toInt() }.sorted()
        return left.zip(right).sumOf { (a,b) -> abs(a - b) }
    }

    override fun part2(input: String): Int {
        val splits = input.lines()
            .filter{ it.isNotBlank() }
            .map{
                it.split("   ")
            }
        val left = splits.map { it[0].toInt() }
        val right = splits.map { it[1].toInt() }
            .groupBy { it }
            .mapValues{(_,v) -> v.size }
        return left.sumOf { it * (right[it] ?: 0) }
    }
}
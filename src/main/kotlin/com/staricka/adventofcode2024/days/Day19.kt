package com.staricka.adventofcode2024.days

import com.staricka.adventofcode2024.framework.Day
import com.staricka.adventofcode2024.util.splitByBlankLines

class Day19: Day {
    private fun parseInput(input: String): Pair<List<String>, List<String>> {
        val split = input.splitByBlankLines()
        val towels = split[0].split(", ").map { it.trim() }
        val patterns = split[1].lines().filter { it.isNotBlank() }.map { it.trim() }
        return towels to patterns
    }

    private val makeMemos = HashMap<Pair<String, Int>, Boolean>()
    private fun canMake(towels: List<String>, pattern: String, index: Int): Boolean {
        if (makeMemos.containsKey(pattern to index)) return makeMemos[pattern to index]!!
        if (index == pattern.length) return true
        val sub = pattern.substring(index)
        return towels.any { sub.startsWith(it) && canMake(towels, pattern, index + it.length) }
            .also { makeMemos[pattern to index] = it }
    }

    private val waysMemos = HashMap<Pair<String, Int>, Long>()
    private fun countWays(towels: List<String>, pattern: String, index: Int): Long {
        if (waysMemos.containsKey(pattern to index)) return waysMemos[pattern to index]!!
        if (index == pattern.length) return 1
        val sub = pattern.substring(index)
        return towels.sumOf { if (sub.startsWith(it)) countWays(towels, pattern, index + it.length) else 0 }
            .also { waysMemos[pattern to index] = it }
    }

    override fun part1(input: String): Int {
        val (towels, patterns) = parseInput(input)
        return patterns.count { canMake(towels, it, 0) }
    }

    override fun part2(input: String): Long {
        val (towels, patterns) = parseInput(input)
        return patterns.sumOf { countWays(towels, it, 0) }
    }
}
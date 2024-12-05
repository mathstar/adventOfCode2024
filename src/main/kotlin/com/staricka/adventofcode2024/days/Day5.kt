package com.staricka.adventofcode2024.days

import com.staricka.adventofcode2024.framework.Day
import com.staricka.adventofcode2024.util.splitByBlankLines

class Day5: Day {
    private fun correctlyOrdered(update: List<Int>, rules: Map<Int, List<Int>>): Boolean {
        val soFar = HashSet<Int>()
        for (n in update) {
            for (pre in rules[n] ?: emptyList()) {
                if (soFar.contains(pre)) {
                    return false
                }
            }
            soFar.add(n)
        }
        return true
    }

    private fun parseInput(input: String): Pair<Map<Int, List<Int>>, List<List<Int>>> {
        val s = input.splitByBlankLines()
        val rules = s[0].lines().filter { it.isNotBlank() }.map { it.split("|").map(String::toInt) }.groupBy({ it[0] }) {it[1]}
        val updates = s[1].lines().filter { it.isNotBlank() }.map{ it.split(",").map(String::toInt) }
        return rules to updates
    }

    override fun part1(input: String): Int {
        val (rules, updates) = parseInput(input)

        var result = 0
        for (update in updates) {
            if (!correctlyOrdered(update, rules)) continue
            result += update[update.size / 2]
        }
        return result
    }

    override fun part2(input: String): Int {
        val (rules, updates) = parseInput(input)

        var result = 0
        for (update in updates) {
            if (correctlyOrdered(update, rules)) continue

            val remaining = HashSet(update)
            val sorted = ArrayList<Int>(update.size)
            while (remaining.isNotEmpty()) {
                val n = remaining.first { rules[it]?.all { post -> !remaining.contains(post) } ?: true }
                sorted.addFirst(n)
                remaining.remove(n)
            }
            result += sorted[sorted.size / 2]
        }
        return result
    }
}
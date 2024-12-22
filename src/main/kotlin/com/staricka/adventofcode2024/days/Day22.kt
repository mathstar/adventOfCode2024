package com.staricka.adventofcode2024.days

import com.staricka.adventofcode2024.framework.Day

class Day22: Day {
    private fun parseInput(input: String): List<Long> = input.lines()
        .filter { it.isNotBlank() }
        .map { it.toLong() }

    private fun mix(secret: Long, value: Long): Long = secret xor value
    private fun prune(secret: Long): Long = secret % 16777216
    private fun step(secret: Long): Long {
        var newSecret = prune(mix(secret, secret * 64))
        newSecret = prune(mix(newSecret, newSecret / 32))
        newSecret = prune(mix(newSecret, newSecret * 2048))
        return newSecret
    }

    private fun getSequenceMap(initialSecret: Long): Map<List<Int>, Int> {
        val sequence = ArrayDeque<Int>()
        var prevPrice = (initialSecret % 10).toInt()
        var secret = initialSecret
        val result = mutableMapOf<List<Int>, Int>()
        for (i in 1..2000) {
            secret = step(secret)
            val price = (secret % 10).toInt()
            sequence.add(price - prevPrice)
            if (sequence.size > 4) sequence.removeFirst()
            if (sequence.size == 4 && !result.containsKey(sequence.toList())) result[sequence.toList()] = price
            prevPrice = price
        }
        return result
    }

    override fun part1(input: String): Long {
        val secrets = parseInput(input)
        return secrets.sumOf {
            var s = it
            for (i in 1..2000) {
                s = step(s)
            }
            s
        }
    }

    override fun part2(input: String): Int {
        val secrets = parseInput(input)
        val sequenceMaps = secrets.map { getSequenceMap(it) }

        val sumMaps = mutableMapOf<List<Int>, Int>()
        sequenceMaps.forEach { m -> m.forEach{ (s, v) -> sumMaps.compute(s) { _, ev -> (ev ?: 0) + v} } }
        return sumMaps.values.max()
    }
}
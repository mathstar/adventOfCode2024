package com.staricka.adventofcode2024.days

import com.staricka.adventofcode2024.framework.Day

class Day11: Day {
    private val memos = HashMap<Pair<Long, Int>, Long>()
    private fun count(value: Long, step: Int, target: Int): Long {
        if (memos.containsKey(value to step)) return memos[value to step]!!
        return if (step == target) {
            1
        } else if (value == 0L) {
            count(1, step + 1, target)
        } else if (value.toString().length % 2 == 0) {
            val s = value.toString()
            count(s.substring(0, s.length/2).toLong(), step + 1, target) + count(s.substring(s.length/2).toLong(), step + 1, target)
        } else {
            count(value * 2024, step + 1, target)
        }.also {
            memos[value to step] = it
        }
    }

    override fun part1(input: String): Long {
        val stones = input.lines().first().split(" ").map { it.toLong() }
        memos.clear()
        return stones.sumOf { count(it, 0, 25) }
    }

    override fun part2(input: String): Long {
        val stones = input.lines().first().split(" ").map { it.toLong() }
        memos.clear()
        return stones.sumOf { count(it, 0, 75) }
    }
}
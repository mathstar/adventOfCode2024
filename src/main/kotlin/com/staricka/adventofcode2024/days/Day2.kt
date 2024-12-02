package com.staricka.adventofcode2024.days

import com.staricka.adventofcode2024.framework.Day
import kotlin.math.abs

class Day2: Day {

    private fun scan(report: List<Int>): Int? {
        val direction = report[1] - report[0] > 0
        for (i in 1 until report.size) {
            if ((report[i] - report[i - 1]) > 0 != direction) {
                return i
            }
            if (abs(report[i] - report[i - 1]) < 1 || abs(report[i] - report[i - 1]) > 3) {
                return i
            }
        }
        return null
    }

    override fun part1(input: String): Int {
        val reports = input.lines().filter { it.isNotEmpty() }
            .map { it.split(" ").map { level -> level.toInt() } }

        var count = 0
        for (report in reports) {
            if (scan(report) == null) {
                count++
            }
        }
        return count
    }

    override fun part2(input: String): Int {
        val reports = input.lines().filter { it.isNotEmpty() }
            .map { it.split(" ").map { level -> level.toInt() } }

        var count = 0
        for (report in reports) {
            if (scan(report) == null) {
                count++
            } else {
                for (i in report.indices) {
                    if (scan(report.subList(0, i) + report.subList(i + 1, report.size)) == null) {
                        count++
                        break
                    }
                }
            }
        }
        return count
    }
}

package com.staricka.adventofcode2024.days

import com.staricka.adventofcode2024.framework.Day
import kotlin.math.abs

class Day2: Day {
    override fun part1(input: String): Any? {
        val reports = input.lines().filter { it.isNotEmpty() }
            .map { it.split(" ").map { level -> level.toInt() } }

        var count = 0
        outer@for (report in reports) {
            val direction = report[1] - report[0] > 0
            for (i in 1 until report.size) {
                if ((report[i] - report[i - 1]) > 0 != direction) {
                    continue@outer
                }
                if (abs(report[i] - report[i - 1]) < 1 || abs(report[i] - report[i-1]) > 3) {
                    continue@outer
                }

            }
            count++
        }
        return count
    }

    override fun part2(input: String): Any? {
        val reports = input.lines().filter { it.isNotEmpty() }
            .map { it.split(" ").map { level -> level.toInt() } }

        var count = 0
        outer@for (report in reports) {
            val direction = report[1] - report[0] > 0
            var dampened = false
            for (i in 1 until report.size) {
                if (((report[i] - report[i - 1]) > 0) != direction) {
                    println("${report[i-1]} ${report[i]} direction")
                    if (dampened) {
                        continue@outer
                    } else {
                        dampened = true
                        continue
                    }
                }
                if (abs(report[i] - report[i - 1]) < 1 || abs(report[i] - report[i-1]) > 3) {
                    println("${report[i-1]} ${report[i]} diff")
                    if (dampened) {
                        continue@outer
                    } else {
                        dampened = true
                    }
                }

            }
            count++
        }
        return count
    }
}

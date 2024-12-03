package com.staricka.adventofcode2024.days

import com.staricka.adventofcode2024.framework.Day
import java.util.regex.Pattern

class Day3 : Day {
    override fun part1(input: String): Int {
        val pattern = Pattern.compile("""mul\(([0-9]+),([0-9]+)\)""")
        val matcher = pattern.matcher(input)
        var result = 0
        while (matcher.find()) {
            result += matcher.group(1).toInt() * matcher.group(2).toInt()
        }
        return result
    }

    override fun part2(input: String): Int {
        val pattern = Pattern.compile("""(mul\(([0-9]+),([0-9]+)\))|(do\(\))|(don't\(\))""")
        val matcher = pattern.matcher(input)
        var result = 0
        var enabled = true
        while (matcher.find()) {
            if (matcher.group().startsWith("mul") && enabled) {
                result += matcher.group(2).toInt() * matcher.group(3).toInt()
            } else if (matcher.group().startsWith("don't")) {
                enabled = false
            } else if (matcher.group().startsWith("do(")) {
                enabled = true
            }
        }
        return result
    }
}
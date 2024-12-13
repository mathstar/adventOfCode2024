package com.staricka.adventofcode2024.days

import com.staricka.adventofcode2024.framework.Day
import com.staricka.adventofcode2024.util.splitByBlankLines
import java.util.regex.Pattern
import kotlin.math.min

class Day13: Day {
    data class Machine(val ax: Long, val ay: Long, val bx: Long, val by: Long, val px: Long, val py: Long) {
        val costMemo = HashMap<Triple<Long, Long, Long>, Long?>()
        fun cost(): Long? = cost(0, 0, 0)
        private fun cost(x: Long, y: Long, costSoFar: Long): Long? {
            if (costMemo.containsKey(Triple(x, y, costSoFar))) return costMemo[Triple(x, y, costSoFar)]

            return if (x == px && y == py)
                costSoFar
            else if (x > px || y > py)
                null
            else {
                val a = cost(x + ax, y + ay, costSoFar + 3)
                val b = cost(x + bx, y + by, costSoFar + 1)
                if (a == null) b
                else if (b == null) a
                else minOf(a, b)
            }.also { costMemo[Triple(x, y, costSoFar)] = it }
        }

        fun costEfficient(): Long? {
            var a = min((px / ax) + 1, (py / ay) + 1)
            var b = 0
            var x = ax * a
            var y = ay * a
            var cost = a * 3
            var minCost = Long.MIN_VALUE
            while (true) {
                while (x + bx > px || y + by > py) {
                    a--
                    x -= ax
                    y -= ay
                    cost -= 3
                }
                if (x == px && y == py) {
                    minCost = min(minCost, cost)
                }
                while (x + bx <= px && y + by <= py) {
                    b++
                    x += bx
                    y += by
                    cost += 1
                }
                if (x == px && y == py) {
                    if (cost < minCost) minCost = cost
                    else return minCost
                }
            }
            return if (minCost == Long.MAX_VALUE) null else minCost
        }

        fun costEfficient2(): Long? {
            var minCost: Long? = null
            for (a in (0..(min(px/ax, py/ay))).reversed()) {
                val xDiff = px - ax * a
                val yDiff = py - ay * a

                if (xDiff % bx == 0L && yDiff % by == 0L &&
                    xDiff / bx == yDiff / by) {
                    System.out.flush()
                    val cost = a * 3 + (xDiff / bx)
                    if (minCost == null || cost < minCost) minCost = cost
                    else return minCost
                    return minCost
                }
            }
            return minCost
        }

        fun add(m: Long) = Machine(ax, ay, bx, by, px + m, py + m)

        companion object {
            val aPattern = Pattern.compile("""Button A: X\+([0-9]+), Y\+([0-9]+)""")
            val bPattern = Pattern.compile("""Button B: X\+([0-9]+), Y\+([0-9]+)""")
            val prizePattern = Pattern.compile("""Prize: X=([0-9]+), Y=([0-9]+)""")
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

    override fun part1(input: String): Any? {
        val machines = input.trim().splitByBlankLines().map { Machine.fromString(it) }
        return machines.sumOf { (it.costEfficient2() ?: 0) }
    }

    override fun part2(input: String): Any? {
        val machines = input.trim().splitByBlankLines().map { Machine.fromString(it) }
        return machines.map { it.add(10000000000000) }.parallelStream().mapToLong { it.costEfficient2() ?: 0 }.sum()
    }
}
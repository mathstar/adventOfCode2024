package com.staricka.adventofcode2024.days

import com.staricka.adventofcode2024.framework.Day

class Day23: Day {
    private fun parseInput(input: String): Map<String, List<String>> {
        val result = mutableMapOf<String, List<String>>()
        input.lines()
            .filter { it.isNotBlank() }
            .forEach {
                val sides = it.split("-")
                result.compute(sides[0]) {_, v -> if (v == null) listOf(sides[1]) else v + sides[1] }
                result.compute(sides[1]) {_, v -> if (v == null) listOf(sides[0]) else v + sides[0] }
            }
        return result
    }

    private fun findTriplets(connections: Map<String, List<String>>): Collection<Set<String>> {
        val result = mutableSetOf<Set<String>>()
        connections.forEach {one, thisConnections ->
            for (i in thisConnections.indices) {
                for (j in i + 1 until thisConnections.size) {
                    if (connections[thisConnections[i]]!!.contains(thisConnections[j])) {
                        result.add(setOf(one, thisConnections[i], thisConnections[j]))
                    }
                }
            }
        }
        return result
    }

    private fun largestGroup(triplets: Collection<Set<String>>, connections: Map<String, List<String>>): Set<String> {
        val groups = triplets.toMutableList()
        var changed = true
        changed@while (changed) {
            changed = false
            for (i in groups.indices) {
                j@for (j in i + 1 until groups.size) {
                    if (groups[i].any { groups[j].contains(it) }) {
                        val combined = (groups[i] + groups[j]).toList()
                        for (ci in combined.indices) {
                            for (cj in ci + 1 until combined.size) {
                                if (!connections[combined[ci]]!!.contains(combined[cj])) continue@j
                            }
                        }
                        val groupI = groups[i]
                        val groupJ = groups[j]
                        groups.remove(groupI)
                        groups.remove(groupJ)
                        groups.add(combined.toSet())
                        changed = true
                        continue@changed
                    }
                }
            }
        }
        return groups.maxBy { it.size }
    }

    override fun part1(input: String): Int {
        return findTriplets(parseInput(input))
            .count { it.any { computer -> computer.startsWith('t') } }
    }

    override fun part2(input: String): String {
        val connections = parseInput(input)
        return largestGroup(findTriplets(connections), connections).sorted().joinToString(",")
    }
}
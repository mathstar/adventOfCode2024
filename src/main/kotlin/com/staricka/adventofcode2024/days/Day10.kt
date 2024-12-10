package com.staricka.adventofcode2024.days

import com.staricka.adventofcode2024.framework.Day
import com.staricka.adventofcode2024.util.Grid
import com.staricka.adventofcode2024.util.GridCell
import com.staricka.adventofcode2024.util.StandardGrid

class Day10: Day {
    data class HikingNode(val height: Int): GridCell {
        override val symbol = '0' + height
    }

    private fun countPeaks(map: Grid<HikingNode>, trailhead: Pair<Int, Int>): Int {
        var points = setOf(trailhead)
        for (height in 1..9) {
            points = points.flatMap { map.manhattanNeighbors(it) }
                .filter { (_,_,h) -> h?.height == height }
                .map { (x,y,_) -> x to y }
                .toSet()
        }
        return points.size
    }

    private fun countPaths(map: Grid<HikingNode>, trailhead: Pair<Int, Int>): Int {
        var points = listOf(trailhead)
        for (height in 1..9) {
            points = points.flatMap { map.manhattanNeighbors(it) }
                .filter { (_,_,h) -> h?.height == height }
                .map { (x,y,_) -> x to y }.toList()
        }
        return points.size
    }

    override fun part1(input: String): Int {
        val map = StandardGrid.build(input) {HikingNode(it - '0')}
        val trailheads = map.cells().filter { (_,_,c) -> c.height == 0 }.map{ it.first to it.second }
        return trailheads.sumOf { countPeaks(map, it) }
    }

    override fun part2(input: String): Int {
        val map = StandardGrid.build(input) {HikingNode(it - '0')}
        val trailheads = map.cells().filter { (_,_,c) -> c.height == 0 }.map{ it.first to it.second }
        return trailheads.sumOf { countPaths(map, it) }
    }
}
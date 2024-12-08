package com.staricka.adventofcode2024.days

import com.staricka.adventofcode2024.framework.Day
import com.staricka.adventofcode2024.util.GridCell
import com.staricka.adventofcode2024.util.StandardGrid

class Day8: Day {
    sealed class Cell : GridCell
    data class Antenna(override val symbol: Char): Cell()
    class Empty: Cell() {
        override val symbol = '.'
    }

    override fun part1(input: String): Int {
        val grid = StandardGrid.build(input){when(it) {
            '.' -> Empty()
            else -> Antenna(it)
        } }
        val antennaMap = grid.cells()
            .filter { (_,_,c) -> c is Antenna }
            .groupBy { (_,_,c) -> c }

        val antinodes = HashSet<Pair<Int, Int>>()
        for ((_, antennas) in antennaMap) {
            for (i in antennas.indices) {
                for (j in (i+1) until antennas.size) {
                    val xDist = antennas[i].first - antennas[j].first
                    val yDist = antennas[i].second - antennas[j].second

                    antinodes.add((antennas[i].first + xDist) to (antennas[i].second + yDist))
                    antinodes.add((antennas[j].first - xDist) to (antennas[j].second - yDist))
                }
            }
        }
        return antinodes.count { it.first in (grid.minX..grid.maxX) && it.second in (grid.minY..grid.maxY) }
    }

    override fun part2(input: String): Int {
        val grid = StandardGrid.build(input){when(it) {
            '.' -> Empty()
            else -> Antenna(it)
        } }
        val antennaMap = grid.cells()
            .filter { (_,_,c) -> c is Antenna }
            .groupBy { (_,_,c) -> c }

        val antinodes = HashSet<Pair<Int, Int>>()
        for ((_, antennas) in antennaMap) {
            for (i in antennas.indices) {
                for (j in (i+1) until antennas.size) {
                    val xDist = antennas[i].first - antennas[j].first
                    val yDist = antennas[i].second - antennas[j].second

                    var x = antennas[i].first
                    var y = antennas[i].second
                    while (x in (grid.minX..grid.maxX) && y in (grid.minY..grid.maxY)) {
                        antinodes.add(x to y)
                        x -= xDist
                        y -= yDist
                    }
                    x = antennas[i].first
                    y = antennas[i].second
                    while (x in (grid.minX..grid.maxX) && y in (grid.minY..grid.maxY)) {
                        antinodes.add(x to y)
                        x += xDist
                        y += yDist
                    }
                }
            }
        }
        return antinodes.count()
    }
}
package com.staricka.adventofcode2024.days

import com.staricka.adventofcode2024.framework.Day
import com.staricka.adventofcode2024.util.GridCell
import com.staricka.adventofcode2024.util.StandardGrid

class Day12: Day {
    data class Plot(override val symbol: Char): GridCell

    private fun findRegions(input: String): Pair<StandardGrid<Plot>, ArrayList<List<Pair<Int, Int>>>> {
        val grid = StandardGrid.build(input) { Plot(it) }
        val visited = HashSet<Pair<Int, Int>>()
        val regions = ArrayList<List<Pair<Int, Int>>>()

        for ((x, y, c) in grid.cells()) {
            if (visited.contains(x to y)) continue

            val region = ArrayList<Pair<Int, Int>>()
            regions.add(region)
            val queue = ArrayDeque<Pair<Int, Int>>()
            queue.add(x to y)
            visited.add(x to y)
            while (queue.isNotEmpty()) {
                val (ax, ay) = queue.removeFirst()
                region.add(ax to ay)
                grid.manhattanNeighbors(ax, ay)
                    .filter { (_, _, bc) -> bc?.symbol == c.symbol }
                    .filter { (bx, by) -> !visited.contains(bx to by) }
                    .forEach { (bx, by, _) ->
                        queue.add(bx to by)
                        visited.add(bx to by)
                    }
            }
        }
        return Pair(grid, regions)
    }

    private fun area(region: List<Pair<Int, Int>>) = region.size
    private fun perimeter(grid: StandardGrid<Plot>, region: List<Pair<Int, Int>>): Int {
        return region.sumOf { (x,y) ->
            grid.manhattanNeighbors(x, y)
                .count{ (_,_,c) -> c?.symbol != grid[x, y]?.symbol}
        }
    }
    private fun sides(grid: StandardGrid<Plot>, region: List<Pair<Int, Int>>): Int {
        val plant = grid[region.first()]?.symbol
        val tops = region.filter { (x,y) -> grid[x - 1, y]?.symbol != plant}
            .sortedWith(Comparator.comparing<Pair<Int, Int>?, Int?> { (x, _) -> x }
                .thenComparing(Comparator.comparing { (_,y) -> y}))
        val bottoms = region.filter { (x,y) -> grid[x + 1, y]?.symbol != plant}
            .sortedWith(Comparator.comparing<Pair<Int, Int>?, Int?> { (x, _) -> x }
                .thenComparing(Comparator.comparing { (_,y) -> y}))
        val lefts = region.filter { (x,y) -> grid[x, y - 1]?.symbol != plant}
            .sortedWith(Comparator.comparing<Pair<Int, Int>?, Int?> { (_,y) -> y }
                .thenComparing(Comparator.comparing { (x, _) -> x }))
        val rights = region.filter { (x,y) -> grid[x, y + 1]?.symbol != plant}
            .sortedWith(Comparator.comparing<Pair<Int, Int>?, Int?> { (_,y) -> y }
                .thenComparing(Comparator.comparing { (x, _) -> x }))

        var sides = 4

        var lastTop = tops.first()
        for (top in tops.drop(1)) {
            if (top.first != lastTop.first || top.second != lastTop.second + 1) {
                sides++
            }
            lastTop = top
        }

        var lastBottom = bottoms.first()
        for (bottom in bottoms.drop(1)) {
            if (bottom.first != lastBottom.first || bottom.second != lastBottom.second + 1) {
                sides++
            }
            lastBottom = bottom
        }

        var lastLeft = lefts.first()
        for (left in lefts.drop(1)) {
            if (left.first != lastLeft.first + 1 || left.second != lastLeft.second) {
                sides++
            }
            lastLeft = left
        }

        var lastRight = rights.first()
        for (right in rights.drop(1)) {
            if (right.first != lastRight.first + 1 || right.second != lastRight.second) {
                sides++
            }
            lastRight = right
        }

        return sides
    }

    override fun part1(input: String): Int {
        val (grid, regions) = findRegions(input)
        return regions.sumOf { area(it) * perimeter(grid, it) }
    }

    override fun part2(input: String): Int {
        val (grid, regions) = findRegions(input)
        return regions.sumOf { area(it) * sides(grid, it) }
    }
}
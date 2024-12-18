package com.staricka.adventofcode2024.days

import com.staricka.adventofcode2024.framework.Day
import com.staricka.adventofcode2024.util.Grid
import com.staricka.adventofcode2024.util.GridCell
import com.staricka.adventofcode2024.util.StandardGrid
import java.util.PriorityQueue

class Day18(private val gridSize: Int = 70, private val part1Bytes: Int = 1024): Day {
    enum class Cell(override val symbol: Char): GridCell {
        WALL('#')
    }

    fun pathfind(grid: Grid<Cell>): Int {
        val queue = PriorityQueue(Comparator.comparingInt<Triple<Int, Int, Int>?> { it.third })
        queue.add(Triple(0, 0, 0))
        val visited = HashSet<Pair<Int,Int>>()
        while (queue.isNotEmpty()) {
            val (x,y,cost) = queue.remove()
            if (visited.contains(x to y)) continue
            visited.add(x to y)
            if (grid[x,y] == Cell.WALL) continue
            if (x == grid.maxX - 1 && y == grid.maxY - 1) return cost
            for ((xp,yp) in grid.manhattanNeighbors(x,y)) queue.add(Triple(xp,yp,cost+1))
        }
        return -1
    }

    fun parseBytes(input: String) = input.lines().filter { it.isNotBlank() }.map { it.split(",").map { it.toInt() } }
    fun populateInitialGrid(bytes: List<List<Int>>): Grid<Cell> {
        val grid = StandardGrid<Cell>()
        for (i in 0..gridSize) {
            grid[-1, i] = Cell.WALL
            grid[gridSize + 1, i] = Cell.WALL
            grid[i, -1] = Cell.WALL
            grid[i, gridSize + 1] = Cell.WALL
        }
        for (i in 0 until part1Bytes) {
            val (x, y) = bytes[i]
            grid[x, y] = Cell.WALL
        }
        return grid
    }

    fun populateGrid(bytes: List<List<Int>>, lastByte: Int): Grid<Cell> {
        val grid = populateInitialGrid(bytes)
        for (i in part1Bytes..lastByte) {
            val (x, y) = bytes[i]
            grid[x, y] = Cell.WALL
        }
        return grid
    }

    fun findBlockingByte(bytes: List<List<Int>>): String {
        var lower = part1Bytes
        var upper = bytes.size - 1
        while (lower < upper) {
            val pivot = (lower + upper) / 2
            val grid = populateGrid(bytes, pivot)
            if (pathfind(grid) > 0) {
                lower = pivot + 1
            } else {
                upper = pivot
            }
        }
        return bytes[lower].joinToString(",")
    }

    override fun part1(input: String): Int {
        val bytes = parseBytes(input)
        val grid = populateInitialGrid(bytes)
        return pathfind(grid)
    }

    override fun part2(input: String): String {
        val bytes = parseBytes(input)
//        val grid = populateInitialGrid(bytes)
//        var i = part1Bytes
//        while (pathfind(grid) > 0) {
//            val (x, y) = bytes[i++]
//            grid[x, y] = Cell.WALL
//        }
//        return bytes[i-1].joinToString(",")
        return findBlockingByte(bytes)
    }
}
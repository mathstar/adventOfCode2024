package com.staricka.adventofcode2024.days

import com.staricka.adventofcode2024.framework.Day
import com.staricka.adventofcode2024.util.Direction
import com.staricka.adventofcode2024.util.Grid
import com.staricka.adventofcode2024.util.GridCell
import com.staricka.adventofcode2024.util.StandardGrid

class Day16: Day {
    enum class Cell(override val symbol: Char): GridCell {
        WALL('#'), START('S'), END('E'), VIEW('O')
    }

    val cache = HashMap<Pair<Int, Int>, Int>()
    fun pathfind(x: Int, y: Int, heading: Direction, grid: Grid<Cell>, costSoFar: Int): Int? {
        val cacheKey = Pair(x, y)
        if (grid[x,y] == Cell.WALL) return null
        if (grid[x,y] == Cell.END) return costSoFar.also { if (costSoFar < (cache[cacheKey] ?: Int.MAX_VALUE)) cache[cacheKey] = costSoFar }
        if ((cache[cacheKey] ?: Int.MAX_VALUE) < costSoFar) return null
        cache[cacheKey] = costSoFar

        val (vx, vy) = heading.vector
        val forward = pathfind(x + vx, y + vy, heading, grid, costSoFar + 1)
        val clockwiseHeading = heading.clockwise()
        val clockwise = pathfind(x + clockwiseHeading.vector.first, y + clockwiseHeading.vector.second, clockwiseHeading,grid, costSoFar + 1001)
        val counterclockwiseHeading = heading.counterClockwise()
        val counterclockwise = pathfind(x + counterclockwiseHeading.vector.first, y + counterclockwiseHeading.vector.second, counterclockwiseHeading, grid, costSoFar + 1001)

        return listOfNotNull(forward, clockwise, counterclockwise).minOrNull()
    }

//    fun pathfindForViewing(x: Int, y: Int, heading: Direction, grid: Grid<Cell>, costSoFar: Int): Pair<Set<Pair<Int, Int>>, Int>? {
//        if (grid[x,y] == Cell.WALL) return null
//
//        val cacheKey = Triple(x, y, heading)
//        if (grid[x,y] == Cell.END) return (setOf(x to y) to costSoFar).also { if (costSoFar < cache[cacheKey]?: Int.MAX_VALUE) cache[cacheKey] = costSoFar }
//        if ((cache[cacheKey] ?: Int.MAX_VALUE) < costSoFar) return null
//        cache[cacheKey] = costSoFar
//
//        val (vx, vy) = heading.vector
//        val forward = pathfindForViewing(x + vx, y + vy, heading, grid, costSoFar + 1)
//        val clockwiseHeading = heading.clockwise()
//        val clockwise = pathfindForViewing(x + clockwiseHeading.vector.first, y + clockwiseHeading.vector.second, clockwiseHeading, grid, costSoFar + 1001)
//        val counterclockwiseHeading = heading.counterClockwise()
//        val counterclockwise = pathfindForViewing(x + counterclockwiseHeading.vector.first, y + counterclockwiseHeading.vector.second, counterclockwiseHeading, grid, costSoFar + 1001)
//
//        val min = listOfNotNull(forward, clockwise, counterclockwise).minByOrNull { it.second }
//        if (min == null) return null
//        return (listOf(x to y) + listOfNotNull(forward, clockwise, counterclockwise).filter { it.second == min.second }.flatMap { it.first }).toSet() to min.second
//    }

//    fun pathfindForViewingTwo(x: Int, y: Int, heading: Direction, grid: Grid<Cell>, costSoFar: Int): Pair<Set<Pair<Int,Int>>, Int>? {
//        val cacheKey = Triple(x, y, heading)
//        if (grid[x,y] == Cell.WALL) return null
//        if (grid[x,y] == Cell.END) return (setOf(x to y) to costSoFar).also { if (costSoFar < cache[cacheKey]?: Int.MAX_VALUE) cache[cacheKey] = costSoFar }
//        if ((cache[cacheKey] ?: Int.MAX_VALUE) < costSoFar) return null
//        cache[cacheKey] = costSoFar
//
//        val (vx, vy) = heading.vector
//        val forward = pathfindForViewingTwo(x + vx, y + vy, heading, grid, costSoFar + 1)
//        val clockwiseHeading = heading.clockwise()
//        val clockwise = pathfindForViewingTwo(x + clockwiseHeading.vector.first, y + clockwiseHeading.vector.second, clockwiseHeading,grid, costSoFar + 1001)
//        val counterclockwiseHeading = heading.counterClockwise()
//        val counterclockwise = pathfindForViewingTwo(x + counterclockwiseHeading.vector.first, y + counterclockwiseHeading.vector.second, counterclockwiseHeading, grid, costSoFar + 1001)
//
//        val min = listOfNotNull(forward, clockwise, counterclockwise).map { it.second }.minOrNull()
//        if (min == null) return null
//        return (setOf(x to y) to min)
//    }

//    fun canBacktrack(fx: Int, fy: Int, tx: Int, ty: Int): Boolean {
//        for (direction in Direction.values()) {
//            if (fx + direction.vector.first == tx && fy + direction.vector.second == ty) {
//                val fkey = Triple(fx, fy, direction)
//                val tkey = Triple(tx, ty, direction)
//                if (cache[fkey] != null && cache[tkey] != null
//                    && cache[fkey]!! + 1 == cache[tkey]) {
//                    return true
//                }
//            }
//            val clockwise = direction.clockwise()
//            if (fx + clockwise.vector.first == tx && fy + clockwise.vector.second == ty) {
//                val fkey = Triple(fx, fy, clockwise)
//                val tkey = Triple(tx, ty, clockwise)
//                if (cache[fkey] != null && cache[tkey] != null
//                    && cache[fkey]!! + 1001 == cache[tkey]) {
//                    return true
//                }
//            }
//            val counterclockwise = direction.counterClockwise()
//            if (fx + counterclockwise.vector.first == tx && fy + counterclockwise.vector.second == ty) {
//                val fkey = Triple(fx, fy, counterclockwise)
//                val tkey = Triple(tx, ty, counterclockwise)
//                if (cache[fkey] != null && cache[tkey] != null
//                    && cache[fkey]!! + 1001 == cache[tkey]) {
//                    return true
//                }
//            }
//        }
//        return false
//    }
//
//    fun backtrack(p: Pair<Int, Int>, grid: Grid<Cell>): Set<Pair<Int, Int>> {
//        return Direction.values()
//            .map{p.first + it.vector.first to p.second + it.vector.second}
//            .filter { grid[it.first, it.second] != Cell.WALL }
//            .filter {
//                canBacktrack(it.first, it.second, p.first, p.second)
//            }
//            .flatMap { backtrack(it, grid) }.toSet() + p
//    }

    override fun part1(input: String): Any? {
        val grid = StandardGrid.build(input.trim()){when (it) {
            '#' -> Cell.WALL
            'S' -> Cell.START
            'E' -> Cell.END
            else -> null
        } }

        val (x,y) = grid.cells().first { (_,_,c) -> c == Cell.START }
        cache.clear()
        return pathfind(x, y, Direction.RIGHT, grid, 0)
    }

    override fun part2(input: String): Any? {
        val grid = StandardGrid.build(input.trim()){when (it) {
            '#' -> Cell.WALL
            'S' -> Cell.START
            'E' -> Cell.END
            else -> null
        } }

        val (sx,sy) = grid.cells().first { (_,_,c) -> c == Cell.START }
        val (ex,ey) = grid.cells().first { (_,_,c) -> c == Cell.END }
        cache.clear()
//        val onPath = pathfindForViewing(sx, sy, Direction.RIGHT, grid, 0)!!.first
//        for ((x,y) in onPath) grid[x,y] = Cell.VIEW
//        return onPath.size
//        pathfind(sx, sy, Direction.RIGHT, grid, 0)
//        val backtrack = backtrack(ex to ey, grid)
//        for ((x,y) in backtrack) grid[x,y] = Cell.VIEW
//        return (backtrack(ex to ey, grid)).size
        return null
    }
}
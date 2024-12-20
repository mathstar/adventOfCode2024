package com.staricka.adventofcode2024.days

import com.staricka.adventofcode2024.framework.Day
import com.staricka.adventofcode2024.util.Grid
import com.staricka.adventofcode2024.util.GridCell
import com.staricka.adventofcode2024.util.StandardGrid
import java.util.ArrayList
import java.util.PriorityQueue
import kotlin.collections.HashSet

class Day20: Day {
    enum class Cell(override val symbol: Char): GridCell {
        WALL('#'), START('S'), END('E')
    }

    fun start(grid: Grid<Cell>) = grid.cells().first { (_,_,c) -> c == Cell.START }

    data class PathfindCheatlessResult(val time: Int, val cheatCandidates: Set<Triple<Int, Int, Int>>)
    fun pathFindWithoutCheat(grid: Grid<Cell>): PathfindCheatlessResult {
        val queue = PriorityQueue<Triple<Int, Int, Int>>(Comparator.comparingInt { it.third })
        val visited = HashSet<Pair<Int, Int>>()
        val (sx,sy) = start(grid)
        queue.add(Triple(sx, sy, 0))
        visited.add(sx to sy)

        val cheatCandidates = HashSet<Triple<Int, Int, Int>>()

        while (queue.isNotEmpty()) {
            val (x,y,t) = queue.remove()
            if (grid[x,y] == Cell.END) {
                return PathfindCheatlessResult(t, cheatCandidates)
            }

            grid.manhattanNeighbors(x,y).forEach { (nx,ny,c) ->
                if (c == Cell.WALL) {
                    //cheatCandidates.add(Triple(nx, ny, t + 1))
                } else if (!visited.contains(nx to ny)) {
                    queue.add(Triple(nx, ny, t + 1))
                    visited.add(nx to ny)
                    cheatCandidates.add(Triple(nx, ny, t + 1))
                }
            }
        }
        return PathfindCheatlessResult(-1, cheatCandidates)
    }

    fun pathFindWithCheat(grid: Grid<Cell>, start: Pair<Int, Int>, initialTime: Int): Int {
        if (grid[start.first, start.second] == Cell.END) return initialTime
        val queue = PriorityQueue<Triple<Int, Int, Int>>(Comparator.comparingInt { it.third })
        val visited = HashSet<Pair<Int, Int>>()
        val (sx,sy) = start
        queue.add(Triple(sx, sy, initialTime))
        visited.add(sx to sy)

        while (queue.isNotEmpty()) {
            val (x,y,t) = queue.remove()
            if (grid[x,y] == Cell.END) {
                return t
            }

            grid.manhattanNeighbors(x,y).forEach { (nx,ny,c) ->
                if (c != Cell.WALL && !visited.contains(nx to ny)) {
                    queue.add(Triple(nx, ny, t + 1))
                    visited.add(nx to ny)
                }
            }
        }
        return Int.MAX_VALUE
    }

    data class Cheat(val start: Pair<Int, Int>, val end: Pair<Int, Int>)
    fun evaluateCheats(pathfindCheatlessResult: PathfindCheatlessResult, grid: Grid<Cell>): Map<Int, List<Cheat>> {
        val (baseCase, cheatCandidates) = pathfindCheatlessResult
        val result = HashMap<Int, ArrayList<Cheat>>()
        val evaluated = HashSet<Pair<Int, Int>>()
        for ((csx, csy, time) in cheatCandidates.sortedBy { it.third }) {
            if (csx <= grid.minX || csx >= grid.maxX || csy <= grid.minY || csy >= grid.maxY) continue
            for ((cex, cey) in grid.manhattanNeighbors(csx, csy)) {
                if (cex <= grid.minX || cex >= grid.maxX || cey <= grid.minY || cey >= grid.maxY) continue
                if (!evaluated.add(cex to cey)) continue
                val cheatCase = pathFindWithCheat(grid, cex to cey, time + 1)
                if (cheatCase < baseCase) {
                    result.computeIfAbsent(baseCase - cheatCase){ArrayList()}.add(Cheat(csx to csy, cex to cey))
                }
            }
        }
        return result
    }

    override fun part1(input: String): Any? {
        val grid = StandardGrid.build(input.trim()) {when (it) {
            '#' -> Cell.WALL
            'S' -> Cell.START
            'E' -> Cell.END
            else -> null
        }}
        return evaluateCheats(pathFindWithoutCheat(grid), grid).filter { (k,_) -> k >= 100 }.values.sumOf { it.size }
    }

    override fun part2(input: String): Any? {
        TODO("Not yet implemented")
    }
}
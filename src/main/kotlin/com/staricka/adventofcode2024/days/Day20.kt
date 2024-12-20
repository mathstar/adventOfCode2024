package com.staricka.adventofcode2024.days

import com.staricka.adventofcode2024.framework.Day
import com.staricka.adventofcode2024.util.Grid
import com.staricka.adventofcode2024.util.GridCell
import com.staricka.adventofcode2024.util.StandardGrid
import java.util.PriorityQueue
import kotlin.collections.ArrayList
import kotlin.collections.HashSet
import kotlin.math.abs

class Day20(private val part1Evaluation: Int = 100, private val part2Evaluation: Int = 100): Day {
    enum class Cell(override val symbol: Char): GridCell {
        WALL('#'), START('S'), END('E')
    }

    data class MazeAnalysis(
        val distanceFromStart: Map<Pair<Int, Int>, Int>,
        val start: Pair<Int, Int>,
        val end: Pair<Int, Int>
    ) {
        companion object {
            private fun start(grid: Grid<Cell>): Pair<Int, Int> {
                val (x, y) = grid.cells().first { (_,_,c) -> c == Cell.START }
                return x to y
            }

            private fun end(grid: Grid<Cell>): Pair<Int, Int> {
                val (x, y) = grid.cells().first { (_,_,c) -> c == Cell.END }
                return x to y
            }

            fun analyzeMaze(grid: Grid<Cell>): MazeAnalysis {
                val distanceFromStart = HashMap<Pair<Int, Int>, Int>()

                val (sx, sy) = start(grid)
                val queue = PriorityQueue<Triple<Int, Int, Int>>(Comparator.comparingInt { it.third })
                val visited = HashSet<Pair<Int, Int>>()
                queue.add(Triple(sx, sy, 0))
                visited.add(Pair(sx, sy))
                while (queue.isNotEmpty()) {
                    val (x, y, t) = queue.remove()
                    distanceFromStart[x to y] = t

                    for ((nx, ny, c) in grid.manhattanNeighbors(x,y)) {
                        if (c != Cell.WALL && !visited.contains(nx to ny)) {
                            queue.add(Triple(nx, ny, t + 1))
                            visited.add(nx to y)
                        }
                    }
                }

                return MazeAnalysis(distanceFromStart, sx to sy, end(grid))
            }
        }
    }

    private fun cheatDestinations(mazeAnalysis: MazeAnalysis, start: Pair<Int, Int>, distance: Int): List<Pair<Int, Int>> {
        return mazeAnalysis.distanceFromStart.keys
            .filter { distance(start, it) <= distance}
    }

    private fun distance(start: Pair<Int, Int>, end: Pair<Int, Int>): Int {
        return abs(start.first - end.first) + abs(start.second - end.second)
    }

    private fun evaluateCheats(grid: Grid<Cell>, distance: Int): Map<Int, List<Cheat>> {
        val mazeAnalysis = MazeAnalysis.analyzeMaze(grid)
        val baseCase = mazeAnalysis.distanceFromStart[mazeAnalysis.end]!!
        val result = HashMap<Int, ArrayList<Cheat>>()

        for (cheatStart in mazeAnalysis.distanceFromStart.keys) {
            for (cheatDestination in cheatDestinations(mazeAnalysis, cheatStart, distance)) {
                val cheatCase = mazeAnalysis.distanceFromStart[cheatStart]!! +
                        (baseCase - mazeAnalysis.distanceFromStart[cheatDestination]!!) +
                        distance(cheatStart, cheatDestination)
                if (cheatCase < baseCase) {
                    result.computeIfAbsent(baseCase - cheatCase){ArrayList()}.add(Cheat(cheatStart, cheatDestination))
                }
            }
        }
        return result
    }

    data class Cheat(val start: Pair<Int, Int>, val end: Pair<Int, Int>)

    override fun part1(input: String): Int {
        val grid = StandardGrid.build(input.trim()) {when (it) {
            '#' -> Cell.WALL
            'S' -> Cell.START
            'E' -> Cell.END
            else -> null
        }}

        return evaluateCheats(grid, 2)
            .filter { (k,_) -> k >= part1Evaluation }
            .values
            .sumOf { it.size }
    }

    override fun part2(input: String): Int {
        val grid = StandardGrid.build(input.trim()) {when (it) {
            '#' -> Cell.WALL
            'S' -> Cell.START
            'E' -> Cell.END
            else -> null
        }}
        return evaluateCheats(grid, 20)
            .filter { (k,_) -> k >= part2Evaluation }
            .values
            .sumOf { it.size }
    }
}